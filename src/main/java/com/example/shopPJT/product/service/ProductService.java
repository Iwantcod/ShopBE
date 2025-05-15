package com.example.shopPJT.product.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.dto.ReqProductDto;
import com.example.shopPJT.product.dto.ReqUpdateProductInfoDto;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.CategoryRepository;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.productSpec.service.ProductSpecServiceFactory;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSpecServiceFactory productSpecServiceFactory;

    @Value("${app.storage-path}")
    private String storagePath;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository,
                          ProductSpecServiceFactory productSpecServiceFactory) {

        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productSpecServiceFactory = productSpecServiceFactory;
    }

    private ResProductDto toDto(Product product) {
        ResProductDto resProductDto = new ResProductDto();
        resProductDto.setProductId(product.getId());
        resProductDto.setName(product.getName());
        resProductDto.setPrice(product.getPrice());
        resProductDto.setInventory(product.getInventory());
        resProductDto.setProductImageUrl(product.getProductImageUrl());
        resProductDto.setCreatedAt(product.getCreatedAt());
        resProductDto.setLogicalFK(product.getLogicalFK());
        resProductDto.setCategoryId(product.getCategory().getId());
        resProductDto.setUserId(product.getUser().getId());
        resProductDto.setDescriptionImageUrl(product.getDescriptionImageUrl());
        resProductDto.setVolume(product.getVolume());
        return resProductDto;
    }

    @Transactional // 특정 회원의 식별자를 가지는 모든 상품 삭제처리: 회원 탈퇴 시 호출되는 메소드
    public void setAllProductDeleteTrueByUserId(Long userId) {
        if(userId == null) {
            // JWT에 회원 식별자 정보가 존재하지 않는 경우, null 반환
            log.error("Set Product 'isDeleted' FAIL: Cannot find 'User ID' in JWT.");
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }
        // 해당 회원(판매자)이 업로드한 모든 상품을 '삭제' 처리한다.
        productRepository.setAllProductDeleteTrueByUserId(userId);
        entityManager.flush();
        entityManager.clear();
    }


    @Transactional(readOnly = true) // 등록일자 기준 내림차순 정렬하고, 페이징한 결과 응답
    public List<ResProductDto> getAllProductDesc(String categoryName, Integer startOffset) {
        // 프론트로부터 1,2,3,4... 의 값을 받는다. 이 값에 10을 곱한 값이 조회 시작지점이다.

        Optional<Category> category = categoryRepository.findByName(categoryName);
        // url로 전달받은 카테고리가 존재하지 않는 카테고리인 경우 조회 불가
        if(category.isEmpty()) {
            throw new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND);
        }
        if(startOffset == null) {
            startOffset = 0;
        }

        int pageSize = 10;
        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findAllActiveProduct(pageable, category.get().getId());
        if(products.isEmpty()){
            log.error("GET Product FAIL: Cannot find Product.");
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }

        Page<ResProductDto> dtoPage = products.map(this::toDto);
        log.info("GET Post SUCCESS.");
        return dtoPage.getContent(); // Page 객체의 메타데이터 없이 본문 데이터만 반환
    }

    @Transactional(readOnly = true) // 판매량 기준 내림차순 정렬, 페이징한 결과 응답
    public List<ResProductDto> getAllProductVolumeDesc(String categoryName, Integer startOffset) {
        int pageSize = 10;

        if(startOffset == null) {
            startOffset = 0;
        }

        Optional<Category> category = categoryRepository.findByName(categoryName);
        // url로 전달받은 카테고리가 존재하지 않는 카테고리인 경우 조회 불가
        if(category.isEmpty()) {
            throw new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("volume").descending());
        Page<Product> products = productRepository.findAllActiveProduct(pageable, category.get().getId());
        if(products.isEmpty()){
            log.error("GET Product FAIL: Cannot find Product.");
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        Page<ResProductDto> dtoPage = products.map(this::toDto);
        return dtoPage.getContent();
    }


    @Transactional(readOnly = true) // 두번째 인자의 값에 따라 분기하여 가격 순 내림차순(true)/오름차순(false) 조회 결과를 페이징하여 응답
    public List<ResProductDto> getAllProductPrice(String categoryName, Integer startOffset, boolean isDescending) {
        int pageSize = 10;
        Optional<Category> category = categoryRepository.findByName(categoryName);
        // url로 전달받은 카테고리가 존재하지 않는 카테고리인 경우 조회 불가
        if(category.isEmpty()) {
            throw new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND);
        }
        if(startOffset == null) {
            startOffset = 0;
        }

        Pageable pageable;
        if(isDescending){
            // isDescending이 true이면 내림차순 정렬(가격 높은 순 정렬)
            pageable = PageRequest.of(startOffset, pageSize, Sort.by("price").descending());
        } else {
            // isDescending이 false이면 오름차순 정렬(가격 낮은 순 정렬)
            pageable = PageRequest.of(startOffset, pageSize, Sort.by("price").ascending());
        }

        Page<Product> products = productRepository.findAllActiveProduct(pageable, category.get().getId());
        if(products.isEmpty()){
            log.error("GET Product FAIL: Cannot find Product.");
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        Page<ResProductDto> dtoPage = products.map(this::toDto);
        return dtoPage.getContent();
    }

    @Transactional(readOnly = true)
    public ResProductDto getProductById(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()){
            return toDto(product.get());
        } else {
            log.error("GET Product FAIL: Cannot find Product.");
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
    }


    // 이미지 업로드 로직을 분리하여 관리하기 위한 메서드
    private String uploadImage(MultipartFile file) throws IOException {
        if(file == null) {
            return null;
        }

        // 1. 스토리지 기본 경로에 해당하는 디렉토리가 존재하지 않는 경우, 해당 디렉토리 생성
        Files.createDirectories(Paths.get(storagePath));

        // 2. 요청에 포함된 파일 원본의 이름을 추출
        String originalImageName = file.getOriginalFilename();
        if(originalImageName == null){
            // 파일 원본 이름이 존재하지 않는 경우, 게시글 생성 불가(이미지 업로드 불가)
            return null;
        }

        // 3. 파일 확장자 추출
        String productImageExtension = originalImageName.substring(originalImageName.lastIndexOf("."));
        // 4. 스토리지에 저장할 파일명 생성: uuid + 확장자
        String newProductImageName = UUID.randomUUID().toString() + productImageExtension;
        // 5. 파일 저장할 경로 생성
        String productImageFilePath = storagePath + newProductImageName;

        // 6. Input Stream을 이용하여 업로드
        Thumbnails.of(file.getInputStream()) // 업로드할 파일의 입력 스트림 사용
                .size(800, 600)                // 이미지 크기 설정
                .outputQuality(0.7)               // 품질 조절(70%)
                .toFile(productImageFilePath);       // 최종적으로 저장

        // 스토리지에 업로드된 이미지 최종 경로를 반환
        return newProductImageName;
    }

    // 특정 모델 정보가 실제로 해당 테이블에 존재하는지 검증
    private boolean isExistProduct(Long productId, String categoryName) {
        if(categoryName == null || productId == null) {
            return false;
        }

        // 'CategoryName'에 해당하는 테이블에서 productId를 통해 조회를 했을 때 결과물이 있으면 true, 없으면 false
        return productSpecServiceFactory.getStrategy(categoryName).isExist(productId);
    }

    // 상품 정보 추가
    @Transactional
    public boolean addProduct(ReqProductDto reqProductDto) throws IOException {
        String userRole = AuthUtil.getCurrentUserAuthority();
        Long userId = AuthUtil.getSecurityContextUserId(); // 요청에 포함된 jwt의 유저 식별자 필드 값을 userId로 사용
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        if(userRole.equals("ROLE_USER")) {
            // 요청에 포함된 jwt의 권한 정보가 '일반 사용자'면 해당 요청 수행 불가
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty() || reqProductDto.getLogicalFK() == null || reqProductDto.getCategoryId() == null) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        Optional<Category> category = categoryRepository.findById(reqProductDto.getCategoryId());
        if(category.isEmpty()){
            throw new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND);
        }

        String productImageUrl = uploadImage(reqProductDto.getProductImage());
        String descriptionImageUrl = uploadImage(reqProductDto.getDescriptionImage());
        // 두 개의 이미지가 모두 업로드 성공하지 않으면 상품 게시글 생성 실패
        // 두 이미지는 필수요소이다.
        if(productImageUrl == null) {
            throw new ApplicationException(ApplicationError.PRODUCT_IMAGE_UPLOAD_FAIL);
        }
        if(descriptionImageUrl == null) {
            throw new ApplicationException(ApplicationError.PRODUCT_DESCRIPTION_IMAGE_UPLOAD_FAIL);
        }

        // 입력받은 '모델 정보'가 시스템(데이터베이스)에 존재하지 않는 경우 false
        if(!isExistProduct(reqProductDto.getLogicalFK(), category.get().getName())) {
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }

        Product product = new Product();
        product.setName(reqProductDto.getName());
        product.setCategory(category.get());
        product.setLogicalFK(reqProductDto.getLogicalFK());
        product.setPrice(reqProductDto.getPrice());
        product.setInventory(reqProductDto.getInventory());
        product.setUser(user.get());
        product.setDescriptionImageUrl(descriptionImageUrl);
        product.setProductImageUrl(productImageUrl);
        productRepository.save(product);
        return true;
    }

    // 이미지 삭제 로직
    private boolean removeImage(String fileName) {
        String filePath = storagePath + fileName;
        File file = new File(filePath);
        if(file.exists()){
            // 파일 제거 후 제거 여부를 bool 값으로 반환
            boolean deleted = file.delete();
            if(!deleted){
                // 제거 실패 시 false 반환
                return false;
            }
        }
        return true;
    }
    @Transactional // 상품 정보 수정: 이미지 존재하면 변경하는 것으로 간주
    public void updateProduct(Long productId, ReqUpdateProductInfoDto reqUpdateProductInfoDto) throws IOException {
        Long userId = AuthUtil.getSecurityContextUserId();
        Optional<Product> product = productRepository.findById(productId);
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        if(product.isEmpty()) {
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        if(!product.get().getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        if(reqUpdateProductInfoDto.getName() != null) {
            product.get().setName(reqUpdateProductInfoDto.getName());
        }

        // 상품 업데이트 요청에 대표 이미지 혹은 상세페이지 이미지가 포함된 경우, 이미지 '변경' 요청으로 간주.
        if(reqUpdateProductInfoDto.getProductImage() != null) {
            // 기존 상품 대표 이미지 제거
            if(!removeImage(product.get().getProductImageUrl())) {
                throw new ApplicationException(ApplicationError.PRODUCT_IMAGE_REMOVE_FAIL); // 추후 비동기 작업으로 변경할 것
            }
            // 새 상품 대표 이미지 업로드
            String newProductImageUrl = uploadImage(reqUpdateProductInfoDto.getProductImage());
            if(newProductImageUrl == null) {
                throw new ApplicationException(ApplicationError.PRODUCT_IMAGE_UPLOAD_FAIL); // 추후 비동기 작업으로 변경할 것
            }
            product.get().setProductImageUrl(newProductImageUrl);
        }
        if(reqUpdateProductInfoDto.getDescriptionImage() != null) {
            // 기존 상품 상세페이지 이미지 제거
            if(!removeImage(product.get().getDescriptionImageUrl())) {
                throw new ApplicationException(ApplicationError.PRODUCT_DESCRIPTION_IMAGE_REMOVE_FAIL);
            }
            // 새 상품 상세페이지 이미지 업로드
            String newDescriptionImageUrl = uploadImage(reqUpdateProductInfoDto.getDescriptionImage());
            if(newDescriptionImageUrl == null) {
                throw new ApplicationException(ApplicationError.PRODUCT_DESCRIPTION_IMAGE_UPLOAD_FAIL);
            }
            product.get().setDescriptionImageUrl(newDescriptionImageUrl);
        }

        if(reqUpdateProductInfoDto.getPrice() > 0) {
            product.get().setPrice(reqUpdateProductInfoDto.getPrice());
        }

        productRepository.save(product.get());
    }

    @Transactional
    public void offProduct(Long productId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()) {
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        // jwt의 회원 식별자와 상품 게시 회원의 식별자가 일치하지 않으면 삭제 처리 불가
        if(!product.get().getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        product.get().setDeleted(true);
    }

    @Transactional // 재고 증가: 비관적 락(쓰기 락)
    public void increaseInventory(Long productId, Integer quantity) {
        Long userId = AuthUtil.getSecurityContextUserId();
        Optional<Product> product = productRepository.findByIdWithPessimisticLock(productId);
        if(product.isEmpty()) {
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        if(!product.get().getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        int prev = product.get().getInventory();
        product.get().setInventory(prev + quantity);
    }

    @Transactional // 재고 감소: 비관적 락(쓰기 락)
    public void decreaseInventory(Long productId, Integer quantity) {
        Long userId = AuthUtil.getSecurityContextUserId();
        Optional<Product> product = productRepository.findByIdWithPessimisticLock(productId);
        if(product.isEmpty()) {
            throw new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND);
        }
        if(!product.get().getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        int prev = product.get().getInventory();
        if(prev - quantity < 0) {
            // 재고를 감소시켰을 때 음수가 나온다면 올바르지 않은 요청
            throw new ApplicationException(ApplicationError.PRODUCT_QUANTITY_INVALID);
        }
        try {
            Thread.sleep(10000); // 10초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        product.get().setInventory(prev - quantity);
    }

}
