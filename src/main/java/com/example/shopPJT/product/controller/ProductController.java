package com.example.shopPJT.product.controller;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.dto.ReqProductDto;
import com.example.shopPJT.product.dto.ReqUpdateProductInfoDto;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.service.ProductService;
import com.example.shopPJT.recommendedProduct.dto.ResRecommended;
import com.example.shopPJT.recommendedProduct.service.RecommendedProductService;
import com.example.shopPJT.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "상품 API")
public class ProductController {
    private final ProductService productService;
    private final RecommendedProductService recommendedProductService;

    @Autowired
    public ProductController(ProductService productService, RecommendedProductService recommendedProductService) {
        this.productService = productService;
        this.recommendedProductService = recommendedProductService;
    }

    @GetMapping("/{categoryName}/latest/{startOffset}")
    @Operation(summary = "등록일자 최신순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<List<ResProductDto>> getProductLatest(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productService.getAllProductDesc(categoryName, startOffset));
    }

    @GetMapping("/{categoryName}/popular/{startOffset}")
    @Operation(summary = "판매량 높은순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<List<ResProductDto>> getProductPopular(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productService.getAllProductVolumeDesc(categoryName, startOffset));
    }

    @GetMapping("/{categoryName}/lowest-price/{startOffset}")
    @Operation(summary = "낮은 가격순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<List<ResProductDto>> getProductLowestPrice(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productService.getAllProductPrice(categoryName, startOffset, false));
    }

    @GetMapping("/{categoryName}/highest-price/{startOffset}")
    @Operation(summary = "높은 가격순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<List<ResProductDto>> getProductHighestPrice(@PathVariable("categoryName") String categoryName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productService.getAllProductPrice(categoryName, startOffset, true));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 식별자로 상품 조회", description = "삭제 처리된 상품이어도 조회")
    public ResponseEntity<ResProductDto> getProductById(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok().body(productService.getProductById(productId));
    }

    @GetMapping("/name/{productNameKey}/{startOffset}")
    @Operation(summary = "상품 이름 키워드로 상품 조회", description = "카테고리 구분없이 검색, 삭제된 상품은 조회하지 않는다.")
    public ResponseEntity<List<ResProductDto>> getProductByNameKey(@PathVariable("productNameKey") String productNameKey, @PathVariable("startOffset") Integer startOffset) {
        return ResponseEntity.ok().body(productService.getProductByNameKey(productNameKey, startOffset));
    }

    @GetMapping("/seller/{userId}/{startOffset}")
    @Operation(summary = "판매자 식별자를 통해 특정 판매자가 업로드한 모든 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<List<ResProductDto>> getProductListBySellerId(@PathVariable("userId") Long userId, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productService.getProductListByUserId(userId, startOffset));
    }

    @GetMapping("/image") // 스토리지에 저장된 실제 이미지 파일을 반환: 이미지 이름은 URL 쿼리 형식으로 입력받아야 한다.
    @Operation(summary = "실제 이미지 파일 반환", description = "'file'이라는 URI 변수에 이미지 키(확장자 포함한 이름)를 포함해주세요.")
    public ResponseEntity<Resource> getImageFile(@RequestParam("name") String imageName) {
        try {
            Resource image = productService.getImageFile(imageName);
            Path path = Paths.get(image.getURI());
            String contentType = Files.probeContentType(path);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(image);
        } catch (IOException e) {
            throw new ApplicationException(ApplicationError.IMAGE_LOAD_FAIL);
        }
    }

    @GetMapping("/recommend/{usageId}/{budget}")
    @Operation(summary = "용도와 예산에 맞는 추천 견적 정보 반환", description = "견적에 해당하는 모든 상품의 세부 정보를 반환합니다.")
    public ResponseEntity<ResRecommended> getRecommendedProduct(@PathVariable("usageId") Integer usageId, @PathVariable("budget") Integer budget) {
        return ResponseEntity.ok().body(recommendedProductService.getRecommendedProduct(usageId, budget));
    }
}
