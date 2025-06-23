package com.example.shopPJT.recommendedOriginal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqRecommendedOriginalDto {
    @NotNull(message = "cpu 모델 식별자는 필수입니다.")
    private Long cpuSpecId;
    @NotNull(message = "graphic 모델 식별자는 필수입니다.")
    private Long graphicSpecId;
    @NotNull(message = "case 모델 식별자는 필수입니다.")
    private Long caseSpecId;
    @NotNull(message = "mainboard 모델 식별자는 필수입니다.")
    private Long mainboardSpecId;
    @NotNull(message = "power 모델 식별자는 필수입니다.")
    private Long powerSpecId;
    @NotNull(message = "storage 모델 식별자는 필수입니다.")
    private Long storageSpecId;
    @NotNull(message = "cooler 모델 식별자는 필수입니다.")
    private Long coolerSpecId;
    @NotNull(message = "memory 모델 식별자는 필수입니다.")
    private Long memorySpecId;
    @NotNull(message = "용도 식별자는 필수입니다.")
    private Long usageId;
}
