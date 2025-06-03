package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemorySpecDto extends ModelNameDto {
    private String groups;
    private Integer cl;
    private Integer volume;
    private Integer speed;
}
