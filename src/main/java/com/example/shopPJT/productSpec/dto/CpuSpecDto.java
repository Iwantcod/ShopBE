package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CpuSpecDto extends ModelNameDto {
    private Integer coreNum;
    private Integer threadNum;
    private Integer l3cache;
    private Integer boostClock;
}
