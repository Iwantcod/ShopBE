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
public class JoinUserDto {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDate birth;
    private String phone;
    private String address;
    private String zipCode;
}
