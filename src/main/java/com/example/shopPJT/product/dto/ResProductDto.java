package com.example.shopPJT.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class ResProductDto extends ProductHeadDto {
    private Long categoryId;
    private Long logicalFK;
    private LocalDate createdAt;
    private int volume; // 판매량
}