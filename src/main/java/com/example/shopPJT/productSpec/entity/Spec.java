package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@MappedSuperclass
@Getter
public abstract class Spec {
    // spec 테이블의 공통 요소를 묶은 추상 클래스

    @Column(nullable = false, unique = true)
    private String modelName;
    private String manufacturer;
    @Column(insertable = false, updatable = false,
        columnDefinition = "DATE DEFAULT (CURRENT_DATE)") // 튜플 추가 시 데이터베이스 레벨에서 자동으로 추가 일자 정보를 삽입
    private LocalDate createdAt;

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
