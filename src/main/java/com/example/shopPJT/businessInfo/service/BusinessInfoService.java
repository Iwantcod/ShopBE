package com.example.shopPJT.businessInfo.service;

import com.example.shopPJT.businessInfo.dto.ReqBusinessInfoDto;
import com.example.shopPJT.businessInfo.dto.ResBusinessInfoDto;
import com.example.shopPJT.businessInfo.entity.BusinessInfo;
import com.example.shopPJT.businessInfo.repository.BusinessInfoRepository;
import com.example.shopPJT.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BusinessInfoService {
    private final BusinessInfoRepository businessInfoRepository;
    @Autowired
    public BusinessInfoService(BusinessInfoRepository businessInfoRepository) {
        this.businessInfoRepository = businessInfoRepository;
    }

    @Transactional // 판매자 권한 승인
    public boolean approveSellAuth(Long userId) {
        Optional<BusinessInfo> businessInfo = businessInfoRepository.findByUserId(userId);
        if (businessInfo.isEmpty()) {
            return false;
        }
        businessInfo.get().setApproval(true);
        return true;
    }


    @Transactional(readOnly = true) // 특정 회원 식별자를 가지는 행을 찾아서 반환
    public ResBusinessInfoDto getBusinessInfoByUserId(Long userId) {
        Optional<BusinessInfo> businessInfo = businessInfoRepository.findByUserId(userId);
        if (businessInfo.isEmpty()) {
            return null;
        }
        ResBusinessInfoDto resBusinessInfoDto = new ResBusinessInfoDto();
        resBusinessInfoDto.setBusinessId(businessInfo.get().getId());
        resBusinessInfoDto.setUserId(userId);
        resBusinessInfoDto.setBusinessType(businessInfo.get().getBusinessType());
        resBusinessInfoDto.setBusinessNumber(businessInfo.get().getBusinessNumber());
        resBusinessInfoDto.setOfficeAddress(businessInfo.get().getOfficeAddress());
        resBusinessInfoDto.setBankName(businessInfo.get().getBankName());
        resBusinessInfoDto.setBankAccount(businessInfo.get().getBankAccount());
        resBusinessInfoDto.setDepositor(businessInfo.get().getDepositor());
        resBusinessInfoDto.setIsApprove(businessInfo.get().getIsApproval());
        return resBusinessInfoDto;
    }


    @Transactional
    public boolean updateBusinessInfo(ReqBusinessInfoDto reqBusinessInfoDto) {
        Long jwtUserId = AuthUtil.getSecurityContextUserId();
        Optional<BusinessInfo> businessInfo = businessInfoRepository.findByUserId(jwtUserId);
        if (businessInfo.isEmpty()) {
            return false;
        }

        if(reqBusinessInfoDto.getBusinessType() != null) {
            businessInfo.get().setBusinessType(reqBusinessInfoDto.getBusinessType());
        }
        if(reqBusinessInfoDto.getBusinessNumber() != null) {
            businessInfo.get().setBusinessNumber(reqBusinessInfoDto.getBusinessNumber());
        }
        if(reqBusinessInfoDto.getOfficeAddress() != null) {
            businessInfo.get().setOfficeAddress(reqBusinessInfoDto.getOfficeAddress());
        }
        if(reqBusinessInfoDto.getBankName() != null) {
            businessInfo.get().setBankName(reqBusinessInfoDto.getBankName());
        }
        if(reqBusinessInfoDto.getBankAccount() != null) {
            businessInfo.get().setBankAccount(reqBusinessInfoDto.getBankAccount());
        }
        if(reqBusinessInfoDto.getDepositor() != null) {
            businessInfo.get().setDepositor(reqBusinessInfoDto.getDepositor());
        }
        return true;
    }
}
