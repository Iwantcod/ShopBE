package com.example.shopPJT.user.dto;

import com.example.shopPJT.user.entity.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ResUserDto {
    private Long userId;
    private String email;
    private String name;
    private String birth;
    private String phone;
    private String address;
    private String zipCode;
    private String role;
}
