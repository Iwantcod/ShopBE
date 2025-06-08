package com.example.shopPJT.businessInfo.service;

import com.example.shopPJT.businessInfo.dto.ReqBusinessInfoDto;
import com.example.shopPJT.businessInfo.dto.ResBusinessInfoDto;
import com.example.shopPJT.businessInfo.entity.BusinessInfo;
import com.example.shopPJT.businessInfo.repository.BusinessInfoRepository;
import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessInfoService {
    private final BusinessInfoRepository businessInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    public BusinessInfoService(BusinessInfoRepository businessInfoRepository, UserRepository userRepository) {
        this.businessInfoRepository = businessInfoRepository;
        this.userRepository = userRepository;
    }

    private ResBusinessInfoDto toDto(BusinessInfo businessInfo) {
        ResBusinessInfoDto resBusinessInfoDto = new ResBusinessInfoDto();
        resBusinessInfoDto.setBusinessId(businessInfo.getId());
        resBusinessInfoDto.setUserId(businessInfo.getUser().getId());
        resBusinessInfoDto.setBusinessName(businessInfo.getBusinessName());
        resBusinessInfoDto.setBusinessNumber(businessInfo.getBusinessNumber());
        resBusinessInfoDto.setBusinessType(businessInfo.getBusinessType());
        resBusinessInfoDto.setDepositor(businessInfo.getDepositor());
        resBusinessInfoDto.setOfficeAddress(businessInfo.getOfficeAddress());
        resBusinessInfoDto.setBankName(businessInfo.getBankName());
        resBusinessInfoDto.setBankAccount(businessInfo.getBankAccount());
        return resBusinessInfoDto;
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

    @Transactional // 판매자 권한 회수
    public boolean disapproveSellAuth(Long userId) {
        Optional<BusinessInfo> businessInfo = businessInfoRepository.findByUserId(userId);
        if (businessInfo.isEmpty()) {
            return false;
        }
        businessInfo.get().setApproval(false);
        return true;
    }


    @Transactional(readOnly = true) // 특정 회원 식별자를 가지는 행을 찾아서 반환
    public ResBusinessInfoDto getBusinessInfoByUserId(Long userId) {
        BusinessInfo businessInfo = businessInfoRepository.findByUserId(userId).orElseThrow(() ->
                new ApplicationException(ApplicationError.BUSINESSINFO_NOT_FOUND));
        return toDto(businessInfo);
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
        if(reqBusinessInfoDto.getBusinessName() != null) {
            businessInfo.get().setBusinessName(reqBusinessInfoDto.getBusinessName());
            Optional<User> user = userRepository.findById(businessInfo.get().getUser().getId());
            if(user.isEmpty()) {
                return false;
            }
            // 비즈니스 로직 상 판매자의 유저네임은 사업자명과 같아야 하므로, 사업자명 변경 시에만 판매자의 이름이 변경된다.
            user.get().setUsername(reqBusinessInfoDto.getBusinessName());
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<ResBusinessInfoDto> getWaitingApproveSellerList(Integer startOffset) {
        if(startOffset == null) {
            startOffset = 0;
        }
        int pageSize = 15;
        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("businessInfoId").descending());
        Slice<BusinessInfo> businessInfos = businessInfoRepository.findByIsApprovalFalsePaging(pageable);
        Slice<ResBusinessInfoDto> result = businessInfos.map(this::toDto);
        return result.getContent();
    }
}
