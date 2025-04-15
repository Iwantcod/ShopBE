package com.example.shopPJT.user.dto;

import com.example.shopPJT.user.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class JoinUserDto {
    private String email;
    private String password;
    private String name;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDate birth;
    private String phone;
    private String address;
    private String zipCode;
    private RoleType role;
}
