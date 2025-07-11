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
public class JoinUserDto extends JoinDto {
    @NotBlank(message = "유저네임(닉네임)은 필수 정보입니다.")
    private String username;
}
