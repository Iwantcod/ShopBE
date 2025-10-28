package com.example.shopPJT.recommendedProduct.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.service.ProductService;
import com.example.shopPJT.recommendedProduct.dto.RecommendedPick;
import com.example.shopPJT.recommendedProduct.dto.ResRecommended;
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
    private final ProductService productService;
    @Autowired
    public RecommendedProductService(RecommendedProductRepository recommendedProductRepository, ProductService productService) {
        this.recommendedProductRepository = recommendedProductRepository;
        this.productService = productService;
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

    @Transactional
    public ResRecommended getRecommendedProduct(Integer usageId, Integer budget) {
        RecommendedProduct recommended = recommendedProductRepository.findByUsageAndBudget(usageId, budget)
                .or(() -> recommendedProductRepository.findLatestCheap(usageId))
                .orElseThrow(() -> new ApplicationException(ApplicationError.RECOMMENDED_NOT_FOUND));
        ResRecommended resRecommended = new ResRecommended();
        if(recommended.getCpuProduct() != null) {
            resRecommended.setCpuProduct(productService.toDto(recommended.getCpuProduct()));
        }
        if(recommended.getGraphicProduct() != null) {
            resRecommended.setGraphicProduct(productService.toDto(recommended.getGraphicProduct()));
        }
        if(recommended.getCaseProduct() != null) {
            resRecommended.setCaseProduct(productService.toDto(recommended.getCaseProduct()));
        }
        if(recommended.getMemoryProduct() != null) {
            resRecommended.setMemoryProduct(productService.toDto(recommended.getMemoryProduct()));
        }
        if(recommended.getPowerProduct() != null) {
            resRecommended.setPowerProduct(productService.toDto(recommended.getPowerProduct()));
        }
        if(recommended.getMainboardProduct() != null) {
            resRecommended.setMainboardProduct(productService.toDto(recommended.getMainboardProduct()));
        }
        if(recommended.getCoolerProduct() != null) {
            resRecommended.setCoolerProduct(productService.toDto(recommended.getCoolerProduct()));
        }
        if(recommended.getStorageProduct() != null) {
            resRecommended.setStorageProduct(productService.toDto(recommended.getStorageProduct()));
        }
        resRecommended.setTotalPrice(recommended.getTotalPrice());
        return resRecommended;
    }
}
