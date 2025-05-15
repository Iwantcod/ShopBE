package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PowerSpecDto extends ModelNameDto {
    private Integer ratedOutputPower;
    private String groups;
}
