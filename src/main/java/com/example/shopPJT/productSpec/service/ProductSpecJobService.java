package com.example.shopPJT.productSpec.service;

import com.example.shopPJT.productSpec.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductSpecJobService {
    private final CpuSpecRepository cpuSpecRepository;
    private final CaseSpecRepository caseSpecRepository;
    private final CoolerSpecRepository coolerSpecRepository;
    private final GraphicSpecRepository graphicSpecRepository;
    private final MainBoardSpecRepository mainBoardSpecRepository;
    private final MemorySpecRepository memorySpecRepository;
    private final PowerSpecRepository powerSpecRepository;
    private final StorageSpecRepository storageSpecRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 1/3 * *", zone = "Asia/Seoul")
    public void updateAvgPrice() {
        // 3일 간격으로 한국 기준 03시에 각 모델 부품의 평균값 계산
        cpuSpecRepository.updateAvgPrice();
        graphicSpecRepository.updateAvgPrice();
        caseSpecRepository.updateAvgPrice();
        coolerSpecRepository.updateAvgPrice();
        mainBoardSpecRepository.updateAvgPrice();
        memorySpecRepository.updateAvgPrice();
        powerSpecRepository.updateAvgPrice();
        storageSpecRepository.updateAvgPrice();
    }
}
