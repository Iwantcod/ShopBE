package com.example.shopPJT.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqCategoryDto {
    private Integer categoryId;
    @NotBlank(message = "카테고리 이름은 필수 정보입니다.")
    private String categoryName;
}
