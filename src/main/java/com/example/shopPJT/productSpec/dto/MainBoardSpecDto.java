package com.example.shopPJT.productSpec.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MainBoardSpecDto extends ModelNameDto {
    private String chipSetType;
    private String cpuSocket;
    private Integer mosFet;
}
