package com.example.shopPJT.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCartDto {
    private Long productId;
    private String productName;
    private int price;
    private int inventory;
    private Long sellerUserId;
    private Integer categoryId;
    private String productImageUrl; // 대표 이미지: 기본적으로 요청
    private String descriptionImageUrl; // 상세 페이지 이미지: 상세 조회 시 실제 이미지를 요청
    private Long cartId;
    private Integer quantity;
}