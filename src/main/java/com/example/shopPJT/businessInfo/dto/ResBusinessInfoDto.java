package com.example.shopPJT.businessInfo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResBusinessInfoDto {
    private Long businessId;
    private Long userId;
    private String businessType;
    private String businessNumber;
    private String officeAddress;
    private String bankName;
    private String bankAccount;
    private String depositor;
    private String businessName;
    private Boolean isApprove;
}
