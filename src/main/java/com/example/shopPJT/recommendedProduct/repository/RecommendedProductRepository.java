package com.example.shopPJT.recommendedProduct.repository;

import com.example.shopPJT.recommendedProduct.entity.RecommendedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedProductRepository extends JpaRepository<RecommendedProduct, Long> {
}
