package com.example.shopPJT.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductHeadDto {
    private Long productId;
    private String name;
    private int price;
    private int inventory;
    private Long sellerUserId;
    private String productImageUrl; // 대표 이미지: 기본적으로 요청
    private String descriptionImageUrl; // 상세 페이지 이미지: 상세 조회 시 실제 이미지를 요청
}
