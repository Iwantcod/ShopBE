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
    private Long categoryId;
    private Long logicalFK;
    private int price;
    private int inventory;
    private LocalDate createdAt;
    private Long userId;
    private String productImageUrl; // 대표 이미지: 기본적으로 요청
    private String descriptionImageUrl; // 상세 페이지 이미지: 상세 조회 시 실제 이미지를 요청
    private int volume; // 판매량
}