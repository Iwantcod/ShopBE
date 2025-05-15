package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModelNameDto {
    private Long id;
    private String modelName;
    private String manufacturer;

    public ModelNameDto(Long id, String modelName, String manufacturer) {
        this.id = id;
        this.modelName = modelName;
        this.manufacturer = manufacturer;
    }
}
