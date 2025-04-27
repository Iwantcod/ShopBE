package com.example.shopPJT.businessInfo.controller;

import com.example.shopPJT.businessInfo.dto.ReqBusinessInfoDto;
import com.example.shopPJT.businessInfo.dto.ResBusinessInfoDto;
import com.example.shopPJT.businessInfo.service.BusinessInfoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
public class BusinessInfoController {
    private final BusinessInfoService businessInfoService;
    @Autowired
    public BusinessInfoController(BusinessInfoService businessInfoService) {
        this.businessInfoService = businessInfoService;
    }

    @GetMapping("/{userId}") // 회원 식별자를 이용해서 BusinessInfo 테이블에서 행 조회
    @Operation(summary = "회원 식별자를 이용하여 BusinessInfo 조회")
    public ResponseEntity<?> getBusinessInfo(@PathVariable Long userId) {
        ResBusinessInfoDto resBusinessInfoDto = businessInfoService.getBusinessInfoByUserId(userId);
        if (resBusinessInfoDto == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resBusinessInfoDto);
        }
    }


    @PatchMapping // BusinessInfo 테이블 수정: 자기 자신만 가능
    @Operation(summary = "자신의 BusinessInfo 정보 수정", description = "jwt의 userId 정보에 해당하는 BusinessInfo의 정보 수정")
    public ResponseEntity<?> updateBusinessInfo(@ModelAttribute ReqBusinessInfoDto reqBusinessInfoDto) {
        if(businessInfoService.updateBusinessInfo(reqBusinessInfoDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
