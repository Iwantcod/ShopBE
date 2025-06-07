package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "CPU SPEC DTO")
public class CpuSpecDto extends ModelNameDto {
    private String coreNum;
    private String threadNum;
    private Integer l3Cache;
    private Integer boostClock;
    private Integer processSize;
}
