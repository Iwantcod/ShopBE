package com.example.shopPJT.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class JoinDto {
    @NotBlank(message = "이메일은 필수 정보입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "유효하지 않은 이메일 형식입니다."
    )
    private String email;
    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    private String password;
    @NotBlank(message = "이름은 필수 정보입니다.")
    private String name;
    private LocalDate birth;
    @Pattern(
            regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "전화번호는 000-0000-0000 형식이어야 합니다."
    )
    private String phone;
    private String address;
    private String addressDetail;
    private String zipCode;
}
