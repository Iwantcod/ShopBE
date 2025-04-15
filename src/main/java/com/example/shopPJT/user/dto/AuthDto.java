package com.example.shopPJT.user.dto;

import com.example.shopPJT.user.entity.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthDto {
    private Long userId;
    private String email;
    private String password;
    private String roleType;
}
