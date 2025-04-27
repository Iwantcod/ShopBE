package com.example.shopPJT.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ReqProductDto {
    @NotNull
    private String name;
    @NotBlank(message = "카테고리 식별자는 필수 정보입니다.")
    private Long categoryId;
    @NotBlank(message = "상품 모델 식별자는 필수 정보입니다.")
    private Long logicalFK;
    @NotNull
    private int price;
    @NotNull
    private int inventory;
    @NotNull
    private MultipartFile productImage; // 실제 이미지 파일(바이트코드)을 받는다.
    @NotNull
    private MultipartFile descriptionImage; // 실제 이미지 파일(바이트코드)을 받는다.
}