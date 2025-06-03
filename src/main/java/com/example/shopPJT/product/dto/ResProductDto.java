package com.example.shopPJT.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class ResProductDto {
    private Long productId;
    private String name;
    private Integer price;
    private Integer inventory;
    private Long sellerUserId;
    private String productImageUrl; // 대표 이미지: 기본적으로 요청
    private String descriptionImageUrl; // 상세 페이지 이미지: 상세 조회 시 실제 이미지를 요청
    private Integer categoryId;
    private Long logicalFK;
    private LocalDate createdAt;
    private Integer volume; // 판매량
}