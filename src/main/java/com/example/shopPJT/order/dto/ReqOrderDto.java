package com.example.shopPJT.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReqOrderDto {
    @NotNull(message = "주문의 배송지 정보는 필수 정보입니다.")
    private String orderAddress;
    @NotNull(message = "주문하는 회원의 연락처는 필수 정보입니다.")
    private String phone;
    @NotNull(message = "결제 품목 및 수량 정보는 필수 정보입니다.")
    private List<ReqOrderItemsDto> orderItems;
}
