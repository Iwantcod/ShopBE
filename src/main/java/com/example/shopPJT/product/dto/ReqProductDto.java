package com.example.shopPJT.product.dto;

import jakarta.validation.constraints.Min;
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
    @NotNull(message = "상품명은 필수 정보입니다.")
    private String name;
    @NotNull(message = "카테고리 식별자는 필수 정보입니다.")
    private Integer categoryId;
    @NotNull(message = "상품 모델 식별자는 필수 정보입니다.")
    private Long logicalFK;
    @NotNull(message = "가격 정보는 필수 정보입니다.") @Min(1)
    private Integer price;
    @NotNull(message = "재고 수량 정보는 필수 정보입니다.") @Min(1)
    private Integer inventory;
    @NotNull(message = "상품 대표 이미지는 필수입니다.")
    private MultipartFile productImage; // 실제 이미지 파일(바이트코드)을 받는다.
    @NotNull(message = "상품 상세 페이지 이미지는 필수입니다.")
    private MultipartFile descriptionImage; // 실제 이미지 파일(바이트코드)을 받는다.
}