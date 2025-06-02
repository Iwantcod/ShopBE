package com.example.shopPJT.order.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResOrderAuthDto {
    private UUID orderId;
    private Integer amount;

    @Builder
    public ResOrderAuthDto(UUID orderId, Integer amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
