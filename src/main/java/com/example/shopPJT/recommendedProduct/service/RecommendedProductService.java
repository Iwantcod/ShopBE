package com.example.shopPJT.recommendedProduct.service;

import com.example.shopPJT.recommendedOriginal.entity.RecommendedOriginal;
import com.example.shopPJT.recommendedOriginal.repository.RecommendedOriginalRepository;
import com.example.shopPJT.recommendedProduct.dto.RecommendedPick;
import com.example.shopPJT.recommendedProduct.entity.RecommendedProduct;
import com.example.shopPJT.recommendedProduct.repository.RecommendedProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class RecommendedProductService {
    private final RecommendedProductRepository recommendedProductRepository;
    @Autowired
    public RecommendedProductService(RecommendedProductRepository recommendedProductRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
    }

    // 최신화
    @Transactional
    @Scheduled(cron = "0 0 4 1/1 * *", zone = "Asia/Seoul")
    public void updateRecommendedProduct() {
        List<RecommendedPick> bestPickList = recommendedProductRepository.findBestProductPerComponent();
        for(RecommendedPick rp : bestPickList) {
            Integer totalPrice = recommendedProductRepository.getRecommendedTotalPrice(rp.getCpuSpecId(), rp.getGraphicSpecId(), rp.getCaseSpecId(), rp.getMemorySpecId(),
                    rp.getPowerSpecId(), rp.getMainboardSpecId(), rp.getCoolerSpecId(), rp.getStorageSpecId());
            recommendedProductRepository.bulkInsert(rp.getRecommendedOriginalId(), rp.getRecommendedUsageId(),
                    rp.getCpuSpecId(), rp.getGraphicSpecId(), rp.getCaseSpecId(), rp.getMemorySpecId(),
                    rp.getPowerSpecId(), rp.getMainboardSpecId(), rp.getCoolerSpecId(), rp.getStorageSpecId(), totalPrice);
        }
    }
}
