package com.example.shopPJT.recommendedOriginal.repository;

import com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto;
import com.example.shopPJT.recommendedOriginal.entity.RecommendedOriginal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


public interface RecommendedOriginalRepository extends JpaRepository<RecommendedOriginal, Long> {
    @Query("""
            SELECT new com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto(r.recommendedOriginalId, r.estimatePrice,
                        cpu.id, cpu.modelName, graphic.id, graphic.modelName, ca.id, ca.modelName, mainboard.id, mainboard.modelName, power.id, power.modelName,
                        storage.id, storage.modelName, cooler.id, cooler.modelName, memory.id, memory.modelName)
            FROM RecommendedOriginal r
            JOIN r.cpuSpec cpu JOIN r.graphicSpec graphic JOIN r.caseSpec ca JOIN r.mainBoardSpec mainboard
            JOIN r.powerSpec power JOIN r.storageSpec storage JOIN r.coolerSpec cooler JOIN r.memorySpec memory
            WHERE r.recommendedUsage.recommendedUsageId = :usageId AND r.estimatePrice <= :price
            ORDER BY r.estimatePrice DESC
            LIMIT 1
            """)
    ResRecommendedOriginalDto findByUsageIdAndPrice(@Param("usageId") Integer usageId, @Param("price") Integer price);


    @Query("""
            SELECT new com.example.shopPJT.recommendedOriginal.dto.ResRecommendedOriginalDto(r.recommendedOriginalId, r.estimatePrice,
                        cpu.id, cpu.modelName, graphic.id, graphic.modelName, ca.id, ca.modelName, mainboard.id, mainboard.modelName, power.id, power.modelName,
                        storage.id, storage.modelName, cooler.id, cooler.modelName, memory.id, memory.modelName)
            FROM RecommendedOriginal r
            JOIN r.cpuSpec cpu JOIN r.graphicSpec graphic JOIN r.caseSpec ca JOIN r.mainBoardSpec mainboard
            JOIN r.powerSpec power JOIN r.storageSpec storage JOIN r.coolerSpec cooler JOIN r.memorySpec memory
            ON r.recommendedUsage.recommendedUsageId = :usageId
            """)
    Slice<ResRecommendedOriginalDto> findByUsageIdOrderByPrice(Pageable pageable, @Param("usageId") Integer usageId);

}
