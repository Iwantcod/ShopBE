package com.example.shopPJT.businessInfo.entity;

import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BusinessInfo {
    @Id @GeneratedValue @Column(name = "BUSINESSINFO_ID")
    private Long id;

    @JoinColumn(name = "USER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String businessType;
    @Column(nullable = false, unique = true)
    private String businessNumber;
    @Column(nullable = false)
    private String officeAddress;
    @Column(nullable = false)
    private String bankName;
    @Column(nullable = false)
    private String bankAccount;
    @Column(nullable = false)
    private String depositor;
    @Column(nullable = false)
    private boolean isApproval = false;

    public void setUser(User user) {
        this.user = user;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public void setApproval(boolean approval) {
        isApproval = approval;
    }
}
