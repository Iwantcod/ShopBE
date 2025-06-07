package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "POWER SPEC DTO")
public class PowerSpecDto extends ModelNameDto {
    private Integer ratedOutputPower;
    private String groups;
    private String plusGrades;
}
