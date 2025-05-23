package com.example.shopPJT.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartDto {
    @NotNull(message = "장바구니 식별자 정보는 필수입니다.")
    private Long cartId;

    @NotNull(message = "장바구니 수정 수량 정보는 필수입니다.")
    @Min(value = 1, message = "수정 수량 수는 1 이상이어야 합니다.")
    private Integer quantity;
}
