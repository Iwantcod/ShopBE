package com.example.shopPJT.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "USERS")
@Getter
public class User {
    @Id @GeneratedValue @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID pgCustomerKey; // UUID 값 저장

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String oAuth2Id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username; // 일반 사용자 가입 시 애플리케이션 레벨 중복 체크, 판매자 가입 시 중복체크 X(사업자명 중복 가능성 있기 때문)

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String name;

    private LocalDate birth;

    private String address;

    private String addressDetail; // 상세 주소(동 호수 등)

    private String zipCode;

    private String refreshToken;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDeleted = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER; // 기본값: '일반 사용자'로 설정

    @PrePersist // Insert 쿼리 호출 시점 직전에 pgCustomerKey 자동 생성 후 주입
    public void onPrePersist() {
        if(pgCustomerKey == null) {
            pgCustomerKey = UUID.randomUUID();
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setoAuth2Id(String oAuth2Id) {
        this.oAuth2Id = oAuth2Id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
}