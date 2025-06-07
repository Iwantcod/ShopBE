package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "GRAPHIC SPEC DTO")
public class GraphicSpecDto extends ModelNameDto {
    private String chipSetType;
    private String chipSetManufacturer;
    private String series;
    private Integer recommendPower;
    private Integer coreClock;
    private Integer boostClock;
    private Integer vram;
    private String groups;
}
