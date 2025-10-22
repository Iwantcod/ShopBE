package com.example.shopPJT.recommendedOriginal.entity;

import com.example.shopPJT.productSpec.entity.*;
import com.example.shopPJT.recommendedUsage.entity.RecommendedUsage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedOriginal {
    @Id @GeneratedValue @Column(name = "RECOMMENDED_ORIGINAL_ID")
    private Long recommendedOriginalId;
    @Column(nullable = false)
    private Integer estimatePrice; // 견적 예상 가격(근사치)
    @Column(insertable = false, updatable = false,
            columnDefinition = "DATE DEFAULT (CURRENT_DATE)")
    private LocalDate createdAt;

    @JoinColumn(name = "CPUSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CpuSpec cpuSpec;
    @JoinColumn(name = "GRAPHICSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GraphicSpec graphicSpec;
    @JoinColumn(name = "CASESPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CaseSpec caseSpec;
    @JoinColumn(name = "MAINBOARDSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MainBoardSpec mainBoardSpec;
    @JoinColumn(name = "POWERSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PowerSpec powerSpec;
    @JoinColumn(name = "STORAGESPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private StorageSpec storageSpec;
    @JoinColumn(name = "COOLERSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CoolerSpec coolerSpec;
    @JoinColumn(name = "MEMORYSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemorySpec memorySpec;

    @JoinColumn(name = "RECOMMENDED_USAGE_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private RecommendedUsage recommendedUsage;

    @Builder
    public RecommendedOriginal(CpuSpec cpuSpec, GraphicSpec graphicSpec, CaseSpec caseSpec, MainBoardSpec mainBoardSpec, PowerSpec powerSpec, StorageSpec storageSpec, CoolerSpec coolerSpec, MemorySpec memorySpec, RecommendedUsage recommendedUsage) {
        this.cpuSpec = cpuSpec;
        this.graphicSpec = graphicSpec;
        this.caseSpec = caseSpec;
        this.mainBoardSpec = mainBoardSpec;
        this.powerSpec = powerSpec;
        this.storageSpec = storageSpec;
        this.coolerSpec = coolerSpec;
        this.memorySpec = memorySpec;
        this.recommendedUsage = recommendedUsage;
        // 견적 원본 예상 가격: 각 부품 정보 평균 가격들의 총합
        this.estimatePrice = cpuSpec.getAvgPrice() + graphicSpec.getAvgPrice() + caseSpec.getAvgPrice() + mainBoardSpec.getAvgPrice()
                + powerSpec.getAvgPrice() + storageSpec.getAvgPrice() + coolerSpec.getAvgPrice() + memorySpec.getAvgPrice();
    }

    public void setEstimatePrice(Integer estimatePrice) {
        this.estimatePrice = estimatePrice;
    }
}
