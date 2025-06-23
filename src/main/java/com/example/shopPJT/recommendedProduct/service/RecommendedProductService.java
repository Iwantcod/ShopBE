package com.example.shopPJT.recommendedProduct.service;

import com.example.shopPJT.recommendedProduct.repository.RecommendedProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecommendedProductService {
    private final RecommendedProductRepository recommendedProductRepository;
    @Autowired
    public RecommendedProductService(RecommendedProductRepository recommendedProductRepository) {
        this.recommendedProductRepository = recommendedProductRepository;
    }

    //
}
