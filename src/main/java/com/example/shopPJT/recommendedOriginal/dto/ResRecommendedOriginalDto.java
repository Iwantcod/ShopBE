package com.example.shopPJT.recommendedOriginal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResRecommendedOriginalDto {
    private Long recommendedOriginalId;
    private Integer estimatePrice;
    private Long cpuSpecId;
    private String cpuSpecName;
    private Long graphicSpecId;
    private String graphicSpecName;
    private Long caseSpecId;
    private String caseSpecName;
    private Long mainboardSpecId;
    private String mainboardSpecName;
    private Long powerSpecId;
    private String powerSpecName;
    private Long storageSpecId;
    private String storageSpecName;
    private Long coolerSpecId;
    private String coolerSpecName;
    private Long memorySpecId;
    private String memorySpecName;

    // 생성자: 모든 필드를 매핑
    public ResRecommendedOriginalDto(
            Long recommendedOriginalId, Integer estimatePrice,
            Long cpuSpecId, String cpuSpecName,
            Long graphicSpecId, String graphicSpecName,
            Long caseSpecId, String caseSpecName,
            Long mainboardSpecId, String mainboardSpecName,
            Long powerSpecId, String powerSpecName,
            Long storageSpecId, String storageSpecName,
            Long coolerSpecId, String coolerSpecName,
            Long memorySpecId, String memorySpecName
    ) {
        this.recommendedOriginalId = recommendedOriginalId;
        this.estimatePrice = estimatePrice;
        this.cpuSpecId = cpuSpecId;
        this.cpuSpecName = cpuSpecName;
        this.graphicSpecId = graphicSpecId;
        this.graphicSpecName = graphicSpecName;
        this.caseSpecId = caseSpecId;
        this.caseSpecName = caseSpecName;
        this.mainboardSpecId = mainboardSpecId;
        this.mainboardSpecName = mainboardSpecName;
        this.powerSpecId = powerSpecId;
        this.powerSpecName = powerSpecName;
        this.storageSpecId = storageSpecId;
        this.storageSpecName = storageSpecName;
        this.coolerSpecId = coolerSpecId;
        this.coolerSpecName = coolerSpecName;
        this.memorySpecId = memorySpecId;
        this.memorySpecName = memorySpecName;
    }
}
