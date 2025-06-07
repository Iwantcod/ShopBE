package com.example.shopPJT.businessInfo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqBusinessInfoDto {
    private String businessType;
    private String businessNumber;
    private String officeAddress;
    private String bankName;
    private String bankAccount;
    private String depositor;
    private String businessName;
}
