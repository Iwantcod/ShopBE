package com.example.shopPJT.recommendedUsage.service;

import com.example.shopPJT.recommendedUsage.dto.ResRecommendedUsageDto;
import com.example.shopPJT.recommendedUsage.entity.RecommendedUsage;
import com.example.shopPJT.recommendedUsage.repository.RecommendedUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RecommendedUsageService {
    private final RecommendedUsageRepository recommendedUsageRepository;
    @Autowired
    public RecommendedUsageService(RecommendedUsageRepository recommendedUsageRepository) {
        this.recommendedUsageRepository = recommendedUsageRepository;
    }

    private ResRecommendedUsageDto toDto(RecommendedUsage recommendedUsage) {
        ResRecommendedUsageDto resRecommendedUsageDto = new ResRecommendedUsageDto();
        resRecommendedUsageDto.setUsageId(recommendedUsage.getRecommendedUsageId());
        resRecommendedUsageDto.setUsageName(recommendedUsage.getUsageName());
        return resRecommendedUsageDto;
    }

    @Transactional(readOnly = true)
    public List<ResRecommendedUsageDto> getAllUsage() {
        return recommendedUsageRepository.findAll().stream().map(this::toDto).toList();
    }
}
