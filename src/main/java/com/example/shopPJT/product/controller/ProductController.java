package com.example.shopPJT.product.controller;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.dto.ReqProductDto;
import com.example.shopPJT.product.dto.ReqUpdateProductInfoDto;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.service.ProductService;
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
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{categoryName}/latest/{startOffset}")
    @Operation(summary = "등록일자 최신순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<?> getProductLatest(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        List<ResProductDto> productDtoList = productService.getAllProductDesc(categoryName, startOffset);
        if(productDtoList != null) {
            return ResponseEntity.ok(productDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{categoryName}/popular/{startOffset}")
    @Operation(summary = "판매량 높은순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<?> getProductPopular(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        List<ResProductDto> productDtoList = productService.getAllProductVolumeDesc(categoryName, startOffset);
        if(productDtoList != null) {
            return ResponseEntity.ok(productDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{categoryName}/lowest-price/{startOffset}")
    @Operation(summary = "낮은 가격순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<?> getProductLowestPrice(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        List<ResProductDto> productDtoList = productService.getAllProductPrice(categoryName, startOffset, false);
        if(productDtoList != null) {
            return ResponseEntity.ok(productDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{categoryName}/highest-price/{startOffset}")
    @Operation(summary = "높은 가격순 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<?> getProductHighestPrice(@PathVariable("categoryName") String categoryName, @PathVariable Integer startOffset) {
        List<ResProductDto> productDtoList = productService.getAllProductPrice(categoryName, startOffset, true);
        if(productDtoList != null) {
            return ResponseEntity.ok(productDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 식별자로 상품 조회", description = "삭제 처리된 상품이어도 조회")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        ResProductDto productDto = productService.getProductById(productId);
        if(productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/seller/{userId}/{startOffset}")
    @Operation(summary = "판매자 식별자를 통해 특정 판매자가 업로드한 모든 상품 조회", description = "10개씩 페이징하여 조회 결과 제공")
    public ResponseEntity<?> getProductListBySellerId(@PathVariable("userId") Long userId, @PathVariable Integer startOffset) {
        List<ResProductDto> productDtoList = productService.getProductListByUserId(userId, startOffset);
        if(productDtoList != null) {
            return ResponseEntity.ok(productDtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
