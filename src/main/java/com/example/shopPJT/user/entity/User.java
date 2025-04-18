package com.example.shopPJT.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity(name = "USERS")
@Getter
public class User {
    @Id @GeneratedValue @Column(name = "USER_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String oAuth2Id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String name;

    private LocalDate birth;

    private String address;

    private String zipCode;

    private String refreshToken;

    private boolean isDeleted = false;

//    @OneToMany(mappedBy = "user")
//    private List<Product> productList = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setoAuth2Id(String oAuth2Id) {
        this.oAuth2Id = oAuth2Id;
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

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
