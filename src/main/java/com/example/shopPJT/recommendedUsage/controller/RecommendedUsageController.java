package com.example.shopPJT.recommendedUsage.controller;

import com.example.shopPJT.recommendedUsage.dto.ResRecommendedUsageDto;
import com.example.shopPJT.recommendedUsage.service.RecommendedUsageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-usage")
public class RecommendedUsageController {
    private final RecommendedUsageService recommendedUsageService;
    @Autowired
    public RecommendedUsageController(RecommendedUsageService recommendedUsageService) {
        this.recommendedUsageService = recommendedUsageService;
    }

    // 모든 용도 정보를 반환
    @GetMapping("/all")
    @Operation(summary = "모든 용도 정보를 조회")
    public ResponseEntity<List<ResRecommendedUsageDto>> getAllUsage() {
        return ResponseEntity.ok(recommendedUsageService.getAllUsage());
    }

    // 특정 용도 추가(관리자)

    // 특정 용도 이름 수정(관리자)

    // 특정 용도 이름 soft-delete(관리자)

}
