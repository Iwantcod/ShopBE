package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Spec {
    // spec 테이블의 공통 요소를 묶은 추상 클래스

    @Column(nullable = false, unique = true)
    private String ModelName;
    @Column(nullable = false)
    private String manufacturer;
}
