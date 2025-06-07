package com.example.shopPJT.benchmark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResBenchMarkDto {
    private Long benchmarkId;
    private Long cpuSpecId;
    private String cpuModelName;
    private Long graphicSpecId;
    private String graphicModelName;
    private Integer avgFrame1;
    private Integer avgFrame2;
    private Integer avgFrame3;
}
