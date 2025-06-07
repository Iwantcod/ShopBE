package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "STORAGE SPEC DTO")
public class StorageSpecDto extends ModelNameDto {
    private String formFactorType;
    private Integer volume;
    private Integer fanSpeed;
    private String groups;
}
