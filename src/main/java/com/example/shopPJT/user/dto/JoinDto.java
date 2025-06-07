package com.example.shopPJT.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class JoinDto {
    @Email
    @NotBlank(message = "이메일은 필수 정보입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    private String password;
    @NotBlank(message = "이름은 필수 정보입니다.")
    private String name;
    private LocalDate birth;
    private String phone;
    private String address;
    private String zipCode;
}
