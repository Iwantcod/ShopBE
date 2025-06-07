package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "MEMORY SPEC DTO")
public class MemorySpecDto extends ModelNameDto {
    private String groups;
    private Integer cl;
    private Integer volume;
    private Integer speed;
}
