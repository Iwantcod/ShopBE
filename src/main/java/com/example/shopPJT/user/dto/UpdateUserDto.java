package com.example.shopPJT.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDto {
    private Long userId;
    private String password;
    private String name;
    private LocalDate birth;
    private String phone;
    private String address;
    private String zipCode;
}
