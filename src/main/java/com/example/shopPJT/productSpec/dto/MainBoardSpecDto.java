package com.example.shopPJT.productSpec.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "MAINBOARD SPEC DTO")
public class MainBoardSpecDto extends ModelNameDto {
    private String chipSetType;
    private String cpuSocket;
    private Integer mosFet;
    private String groups;
    private String modelGroups;
}
