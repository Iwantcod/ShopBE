package com.example.shopPJT.seller.controller;

import com.example.shopPJT.product.dto.ReqProductDto;
import com.example.shopPJT.product.dto.ReqUpdateProductInfoDto;
import com.example.shopPJT.product.service.ProductService;
import com.example.shopPJT.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/seller")
@Tag(name = "판매자 API", description = "요청에 담긴 jwt의 내부 권한이 ADMIN, SELLER인 경우에만 응답")
public class SellerController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @Autowired
    public SellerController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    // 상품 추가
    @PostMapping(value = "/product",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 추가", description = "대표 이미지 및 상세 이미지 필수. 게시자명은 유저네임(닉네임) 사용")
    public ResponseEntity<Void> addProduct(@Valid @ModelAttribute ReqProductDto reqProductDto) throws IOException {
        if(productService.addProduct(reqProductDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 상품 삭제
    @PatchMapping("/product/{productId}")
    @Operation(summary = "상품 삭제처리", description = "자기 자신이 올린 이미지만 삭제처리 가능")
    public ResponseEntity<Void> offProduct(@PathVariable("productId") Long productId) {
        productService.offProduct(productId);
        return ResponseEntity.ok().build();
    }

    // 상품 게시글 정보 수정
    @PostMapping(value = "/product-update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "상품 정보(이름, 가격, 이미지) 수정", description = "이 요청에 이미지 포함 시, '이미지 변경'으로 간주")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute ReqUpdateProductInfoDto reqUpdateProductInfoDto) {
        productService.updateProduct(productId, reqUpdateProductInfoDto);
        return ResponseEntity.ok().build();
    }

    // 상품 재고 증가
    @PatchMapping("/inven-up/{productId}/{quantity}")
    @Operation(summary = "상품 재고 수량 추가", description = "해당 상품을 게시판 판매자만 요청 가능")
    public ResponseEntity<Void> increaseProductInven(@PathVariable("productId") Long productId, @PathVariable Integer quantity) {
        productService.modifyInventory(productId, quantity, Boolean.TRUE);
        return ResponseEntity.ok().build();
    }

    // 상품 재고 차감
    @PatchMapping("/inven-down/{productId}/{quantity}")
    @Operation(summary = "상품 재고 수량 삭제", description = "해당 상품을 게시판 판매자만 요청 가능")
    public ResponseEntity<Void> decreaseProductInven(@PathVariable Long productId, @PathVariable Integer quantity) {
        productService.modifyInventory(productId, quantity, Boolean.FALSE);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/review/write-permission/{productId}")
    @Operation(summary = "리뷰 답글 작성 권한 확인", description = "자신이 게시한 상품의 리뷰에만 답글을 작성할 수 있습니다.")
    public ResponseEntity<Void> reviewWritePermission(@PathVariable("productId") Long productId) {
        reviewService.checkAnswerWritePermission(productId);
        return ResponseEntity.ok().build();
    }
}
