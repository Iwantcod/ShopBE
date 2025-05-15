package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseSpecDto extends ModelNameDto {
    private Integer size;
    private Integer innerSpace;
}
