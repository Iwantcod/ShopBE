package com.example.shopPJT.product.controller;

import com.example.shopPJT.product.dto.ReqProductDto;
import com.example.shopPJT.product.dto.ReqUpdateProductInfoDto;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 추가", description = "2개 이미지 필수. 등록자 id는 jwt내에 존재하는 id로 사용")
    public ResponseEntity<?> addProduct(@Valid @ModelAttribute ReqProductDto reqProductDto) throws IOException {
        if(productService.addProduct(reqProductDto)) {
            return ResponseEntity.ok().body("상품이 등록되었습니다.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping ("/{productId}")
    @Operation(summary = "상품 삭제처리", description = "자기 자신이 올린 이미지만 삭제처리 가능")
    public ResponseEntity<?> offProduct(@PathVariable("productId") Long productId) {
        productService.offProduct(productId);
        return ResponseEntity.ok().body("상품이 삭제되었습니다.");
    }

    // 상품 게시글 정보 수정
    @PostMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 정보(이름, 가격, 이미지) 수정", description = "이 요청에 이미지 포함 시, '이미지 변경'으로 간주")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute ReqUpdateProductInfoDto reqUpdateProductInfoDto) throws IOException {
        productService.updateProduct(productId, reqUpdateProductInfoDto);
        return ResponseEntity.ok().body("상품 정보가 수정되었습니다.");
    }

    @PatchMapping("/up/{productId}/{quantity}")
    @Operation(summary = "상품 재고 수량 추가", description = "해당 상품을 게시판 판매자만 요청 가능")
    public ResponseEntity<?> increaseProductInven(@PathVariable("productId") Long productId, @PathVariable Integer quantity) {
        productService.increaseInventory(productId, quantity);
        return ResponseEntity.ok().body("상품 재고 수량이 추가되었습니다.");
    }

    @PatchMapping("/down/{productId}/{quantity}")
    @Operation(summary = "상품 재고 수량 삭제", description = "해당 상품을 게시판 판매자만 요청 가능")
    public ResponseEntity<?> decreaseProductInven(@PathVariable Long productId, @PathVariable Integer quantity) {
        productService.decreaseInventory(productId, quantity);
        return ResponseEntity.ok().body("상품 재고 수량이 삭제되었습니다.");
    }

}
