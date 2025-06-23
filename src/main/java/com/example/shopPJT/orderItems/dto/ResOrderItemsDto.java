package com.example.shopPJT.orderItems.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResOrderItemsDto {
    private Long productId;
    private String productName;
    private Long sellerUserId;
    private String productImageUrl; // 상품 대표 이미지
    private Integer quantity;
    private Integer categoryId;
}