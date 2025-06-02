package com.example.shopPJT.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReqApproveOrderDto {
    // 클라이언트가 PG사로 보낸 결제 요청이 성공하였을 때 반환받는 3가지 값. 이 값을 서버로 보내는데, 이때 값을 매핑하기 위한 DTO
    @NotBlank(message = "결제 승인 요청: paymentKey 값은 필수입니다.")
    private String paymentKey;
    @NotNull(message = "결제 승인 요청: orderId 값은 필수입니다.")
    private UUID orderId;
    @NotNull(message = "결제 승인 요청: amount 값은 필수입니다.")
    private Integer amount;
}
