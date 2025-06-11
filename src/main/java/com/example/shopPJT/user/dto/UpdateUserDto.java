package com.example.shopPJT.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDto {
    @NotNull(message = "업데이트 대상 회원 식별자 정보는 필수 정보입니다.")
    private Long userId;

    private String password;
    private String name;
    private LocalDate birth;
    @Pattern(
            regexp = "^\\d{3}-\\d{4}-\\d{4}$",
            message = "전화번호는 000-0000-0000 형식이어야 합니다."
    )
    private String phone;
    private String address;
    private String zipCode;
}
