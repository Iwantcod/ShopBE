package com.example.shopPJT.order.dto;

import com.example.shopPJT.order.entity.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResOrderDto {
    private Long orderId;
    private Integer amount;
    private LocalDateTime requestedAt;
    private String address;
    private String addressDetail;
    private String phone;
    private DeliveryStatus deliveryStatus;

    @Builder
    public ResOrderDto(Long orderId, Integer amount, LocalDateTime requestedAt, String address, String addressDetail, String phone, DeliveryStatus deliveryStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.requestedAt = requestedAt;
        this.address = address;
        this.addressDetail = addressDetail;
        this.phone = phone;
        this.deliveryStatus = deliveryStatus;
    }
}
