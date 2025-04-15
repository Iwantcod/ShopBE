package com.example.shopPJT.user.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinSellerDto extends JoinUserDto {
    private String businessType;
    private String businessNumber;
    private String officeAddress;
    private String bankName;
    private String bankAccount;
    private String depositor;
}
