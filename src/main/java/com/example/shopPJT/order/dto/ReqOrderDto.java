package com.example.shopPJT.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReqOrderDto {
    @NotBlank(message = "주문의 배송지 정보는 필수 정보입니다.")
    private String orderAddress;
    @NotBlank(message = "주문의 배송지 상세 정보는 필수 정보입니다.")
    private String orderAddressDetail;
    @NotBlank(message = "주문하는 회원의 연락처는 필수 정보입니다.")
    @Pattern(
            regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "전화번호는 000-0000-0000 형식이어야 합니다."
    )
    private String phone;
    @NotNull(message = "결제 품목 및 수량 정보는 필수 정보입니다.")
    private List<ReqOrderItemsDto> orderItems;
}
