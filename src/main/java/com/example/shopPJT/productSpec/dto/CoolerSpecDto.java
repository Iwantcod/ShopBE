package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoolerSpecDto extends ModelNameDto {
    private String groups;
    private Integer fanSpeed;
    private Integer noise;
}
