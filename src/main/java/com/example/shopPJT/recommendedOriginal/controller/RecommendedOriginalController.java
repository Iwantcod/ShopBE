package com.example.shopPJT.recommendedOriginal.controller;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto;
import com.example.shopPJT.recommendedOriginal.service.RecommendedOriginalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/original")
@Tag(name = "추천 견적 '원본' API", description = "각 카테고리의 부품 모델 정보로만 구성된 견적 원본 관련 API")
public class RecommendedOriginalController {
    private final RecommendedOriginalService recommendedOriginalService;
    @Autowired
    public RecommendedOriginalController(RecommendedOriginalService recommendedOriginalService) {
        this.recommendedOriginalService = recommendedOriginalService;
    }


    @GetMapping("/price-asc/{usageName}/{startOffset}")
    @Operation(summary = "특정 용도의 추천 견적 가격 순 오름차순 페이징 조회")
    public ResponseEntity<List<ResRecommendedOriginalDto>> getRecommendedOriginalPriceAsc(@PathVariable String usageName, @PathVariable Integer startOffset) {
        if(usageName == null) {
            throw new ApplicationException(ApplicationError.USAGE_NOT_FOUND);
        }
        return ResponseEntity.ok().body(recommendedOriginalService.findAllByUsageOrderByPrice(usageName, startOffset, true));
    }

    @GetMapping("/price-desc/{usageName}/{startOffset}")
    @Operation(summary = "특정 용도의 추천 견적 가격 순 내림차순 페이징 조회")
    public ResponseEntity<List<ResRecommendedOriginalDto>> getRecommendedOriginalPriceDesc(@PathVariable String usageName, @PathVariable Integer startOffset) {
        if(usageName == null) {
            throw new ApplicationException(ApplicationError.USAGE_NOT_FOUND);
        }
        return ResponseEntity.ok().body(recommendedOriginalService.findAllByUsageOrderByPrice(usageName, startOffset, false));
    }

    @GetMapping("/recommend/{usageName}/{budget}")
    @Operation(summary = "용도와 예산에 맞는 견적 원본 조회")
    public ResponseEntity<ResRecommendedOriginalDto> getRecommendedOriginal(@PathVariable String usageName, @PathVariable Integer budget) {
        return ResponseEntity.ok().body(recommendedOriginalService.findByUsageAndPrice(usageName, budget));
    }

}
