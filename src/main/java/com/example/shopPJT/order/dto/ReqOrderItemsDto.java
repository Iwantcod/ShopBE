package com.example.shopPJT.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqOrderItemsDto {
    @NotNull(message = "주문할 상품의 식별자는 필수 정보입니다.")
    private Long productId;
    @NotNull(message = "주문할 상품의 개수 값은 필수 정보입니다.")
    private Integer quantity;
}
