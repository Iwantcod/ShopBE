package com.example.shopPJT.order.entity;

import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Builder 생성자를 이용한 엔티티(인스턴스)생성만을 허용
public class Order {
    @Id @GeneratedValue @Column(name = "ORDER_ID")
    private Long id;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String pgPaymentKey; // pg사에서 발급받은 값을 저장할 컬럼

    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID pgOrderId; // UUID 값 저장: 자동 생성

    private Integer amount; // 총 결제 금액

    @CreationTimestamp
    private LocalDateTime requested_at; // 생성 일자 및 시각

    @Column(nullable = false)
    private String address; // 배송 주소

    @Column(nullable = false)
    private String phone; // 주문자 연락처

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; // 배송 상태

    @Builder
    public Order(User user, String address, String phone) {
        this.user = user;
        this.address = address;
        this.phone = phone;
        this.deliveryStatus = DeliveryStatus.PROCESSING;
    }

    @PrePersist // Insert 쿼리 호출 직전 엔티티의 pgOrderId가 비어있으면 UUID로 생성후 주입
    public void onPrePersist() {
        if(pgOrderId == null) pgOrderId = UUID.randomUUID();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPgPaymentKey(String pgPaymentKey) {
        this.pgPaymentKey = pgPaymentKey;
    }

    public void setPgOrderId(UUID pgOrderId) {
        this.pgOrderId = pgOrderId;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
