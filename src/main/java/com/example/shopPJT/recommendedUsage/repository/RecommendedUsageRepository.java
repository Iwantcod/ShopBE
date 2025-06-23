package com.example.shopPJT.recommendedUsage.repository;

import com.example.shopPJT.recommendedUsage.entity.RecommendedUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendedUsageRepository extends JpaRepository<RecommendedUsage, Long> {
    Optional<RecommendedUsage> findByUsageName(String usageName);
}
