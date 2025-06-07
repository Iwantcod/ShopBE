package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "COOLER SPEC DTO")
public class CoolerSpecDto extends ModelNameDto {
    private String groups;
    private Integer fanSpeed;
    private Integer noise;
}
