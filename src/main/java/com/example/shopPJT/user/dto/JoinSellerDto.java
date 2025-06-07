package com.example.shopPJT.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinSellerDto extends JoinDto {
    @NotBlank(message = "사업자 분류 정보는 필수 정보입니다.")
    private String businessType;
    @NotBlank(message = "사업자 등록번호는 필수 정보입니다.")
    private String businessNumber;
    @NotBlank(message = "사업자 사무실 주소 정보는 필수 정보입니다.")
    private String officeAddress;
    @NotBlank(message = "은행 종류는 필수 정보입니다.")
    private String bankName;
    @NotBlank(message = "계좌번호는 필수 정보입니다.")
    private String bankAccount;
    @NotBlank(message = "예금자명은 필수 정보입니다.")
    private String depositor;
    @NotBlank(message = "상호명은 필수 정보입니다.")
    private String businessName;
}
