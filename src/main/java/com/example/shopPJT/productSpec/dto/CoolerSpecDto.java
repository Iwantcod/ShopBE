package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoolerSpecDto extends ModelNameDto {
    private Integer size;
    private Integer fanSpeed;
    private Integer noise;
    private String groups;
}
