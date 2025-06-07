package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "부품의 모델명과 제조사만 제공하는 간단 DTO")
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
