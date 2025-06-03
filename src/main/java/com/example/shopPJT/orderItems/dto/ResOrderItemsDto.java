package com.example.shopPJT.orderItems.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResOrderItemsDto {
    private Long productId;
    private String name;
    private Long sellerUserId;
    private String productImageUrl; // 상품 대표 이미지
    private Integer quantity;

    public ResOrderItemsDto(Long productId, String name, Long sellerUserId, String productImageUrl, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.sellerUserId = sellerUserId;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
    }
}