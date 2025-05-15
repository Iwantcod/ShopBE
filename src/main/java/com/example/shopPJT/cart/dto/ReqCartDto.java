package com.example.shopPJT.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqCartDto {
    @NotNull(message = "상품 식별자 값은 필수 정보입니다.")
    private Long productId;
    @NotNull(message = "상품 개수 값은 필수 정보입니다.")
    @Min(value = 1, message = "수량 정보는 1 이상이어야 합니다.")
    private Integer quantity;
}
