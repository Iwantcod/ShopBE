package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StorageSpecDto extends ModelNameDto {
    private String formFactorType;
    private Integer volume;
    private Integer fanSpeed;
    private String groups;
}
