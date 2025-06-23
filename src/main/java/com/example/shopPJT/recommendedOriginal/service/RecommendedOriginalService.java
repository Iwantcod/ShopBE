package com.example.shopPJT.recommendedOriginal.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.productSpec.repository.*;
import com.example.shopPJT.recommendedOriginal.dto.ReqRecommendedOriginalDto;
import com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto;
import com.example.shopPJT.recommendedOriginal.entity.RecommendedOriginal;
import com.example.shopPJT.recommendedOriginal.repository.RecommendedOriginalRepository;
import com.example.shopPJT.recommendedUsage.entity.RecommendedUsage;
import com.example.shopPJT.recommendedUsage.repository.RecommendedUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class RecommendedOriginalService {
    private final RecommendedOriginalRepository recommendedOriginalRepository;
    private final RecommendedUsageRepository recommendedUsageRepository;
    private final CpuSpecRepository cpuSpecRepository;
    private final GraphicSpecRepository graphicSpecRepository;
    private final CaseSpecRepository caseSpecRepository;
    private final MainBoardSpecRepository mainBoardSpecRepository;
    private final PowerSpecRepository powerSpecRepository;
    private final StorageSpecRepository storageSpecRepository;
    private final CoolerSpecRepository coolerSpecRepository;
    private final MemorySpecRepository memorySpecRepository;

    @Autowired
    public RecommendedOriginalService(RecommendedOriginalRepository recommendedOriginalRepository, RecommendedUsageRepository recommendedUsageRepository, CpuSpecRepository cpuSpecRepository, GraphicSpecRepository graphicSpecRepository, CaseSpecRepository caseSpecRepository, MainBoardSpecRepository mainBoardSpecRepository, PowerSpecRepository powerSpecRepository, StorageSpecRepository storageSpecRepository, CoolerSpecRepository coolerSpecRepository, MemorySpecRepository memorySpecRepository) {
        this.recommendedOriginalRepository = recommendedOriginalRepository;
        this.recommendedUsageRepository = recommendedUsageRepository;
        this.cpuSpecRepository = cpuSpecRepository;
        this.graphicSpecRepository = graphicSpecRepository;
        this.caseSpecRepository = caseSpecRepository;
        this.mainBoardSpecRepository = mainBoardSpecRepository;
        this.powerSpecRepository = powerSpecRepository;
        this.storageSpecRepository = storageSpecRepository;
        this.coolerSpecRepository = coolerSpecRepository;
        this.memorySpecRepository = memorySpecRepository;
    }

    // 견적 원본 추가, 수정, 삭제는 추천 서버에서 구현: 관리자 권한으로 API 정의는 해놓기
    @Transactional
    public void addOriginal(ReqRecommendedOriginalDto request) {
        // 견적에 해당하는 모든 요소를 추가한다. 모든 과정이 성공하지 않으면 견적 원본 정보를 DB에 삽입할 수 없다.

        CpuSpec cpuSpec = cpuSpecRepository.findById(request.getCpuSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.CPU_NOT_FOUND));
        GraphicSpec graphicSpec = graphicSpecRepository.findById(request.getGraphicSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.GRAPHIC_NOT_FOUND));
        CaseSpec caseSpec = caseSpecRepository.findById(request.getCaseSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.CASE_NOT_FOUND));
        MainBoardSpec mainBoardSpec = mainBoardSpecRepository.findById(request.getMainboardSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.MAINBOARD_NOT_FOUND));
        PowerSpec powerSpec = powerSpecRepository.findById(request.getPowerSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.POWER_NOT_FOUND));
        StorageSpec storageSpec = storageSpecRepository.findById(request.getStorageSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.STORAGE_NOT_FOUND));
        CoolerSpec coolerSpec = coolerSpecRepository.findById(request.getCoolerSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.COOLER_NOT_FOUND));
        MemorySpec memorySpec = memorySpecRepository.findById(request.getMemorySpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.MEMORY_NOT_FOUND));
        RecommendedUsage recommendedUsage = recommendedUsageRepository.findById(request.getUsageId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.USAGE_NOT_FOUND));

        RecommendedOriginal recommendedOriginal = RecommendedOriginal.builder()
                .cpuSpec(cpuSpec)
                .graphicSpec(graphicSpec)
                .caseSpec(caseSpec)
                .mainBoardSpec(mainBoardSpec)
                .powerSpec(powerSpec)
                .storageSpec(storageSpec)
                .coolerSpec(coolerSpec)
                .memorySpec(memorySpec)
                .recommendedUsage(recommendedUsage)
                .build();
        recommendedOriginalRepository.save(recommendedOriginal);
    }


    // 견적 원본 용도별 페이징 조회: 예산 가격 기준 오름차순/내림차순
    @Transactional(readOnly = true)
    public List<ResRecommendedOriginalDto> findAllByUsageOrderByPrice(String usageName, Integer startOffset, boolean isAsc) {
        if(startOffset < 0 || startOffset == null) {
            startOffset = 0;
        }
        int pageSize = 10;
        RecommendedUsage recommendedUsage = recommendedUsageRepository.findByUsageName(usageName).orElseThrow(() ->
                new ApplicationException(ApplicationError.USAGE_NOT_FOUND));


        Pageable pageable;
        // isAsc가 true면 오름차순, 아니라면 내림차순으로 Pageable 설정
        if(isAsc) {
            pageable = PageRequest.of(startOffset, pageSize, Sort.by("estimatePrice").ascending());
        } else {
            pageable = PageRequest.of(startOffset, pageSize, Sort.by("estimatePrice").descending());
        }
        Slice<ResRecommendedOriginalDto> result = recommendedOriginalRepository.findByUsageIdOrderByPrice(pageable, recommendedUsage.getRecommendedUsageId());
        if(result.isEmpty()) {
            throw new ApplicationException(ApplicationError.RECOMMENDED_NOT_FOUND);
        }
        return result.getContent();
    }

    // 추천 견적 조회: 용도 및 가격
    @Transactional(readOnly = true)
    public ResRecommendedOriginalDto findByUsageAndPrice(String usageName, Integer budget) {
        if(budget < 75) {
            budget = 75;
        }
        RecommendedUsage recommendedUsage = recommendedUsageRepository.findByUsageName(usageName).orElseThrow(() ->
                new ApplicationException(ApplicationError.USAGE_NOT_FOUND));
        ResRecommendedOriginalDto result = recommendedOriginalRepository.findByUsageIdAndPrice(recommendedUsage.getRecommendedUsageId(), budget);
        if(result == null) {
            throw new ApplicationException(ApplicationError.RECOMMENDED_NOT_FOUND);
        }
        return result;
    }

}
