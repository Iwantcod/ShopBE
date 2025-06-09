package com.example.shopPJT.benchmark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqBenchMarkDto extends BenchMarkDto {
    @NotNull(message = "CPU 식별자는 필수 정보입니다.")
    private Long cpuSpecId;
    @NotNull(message = "GRAPHIC 식별자는 필수 정보입니다.")
    private Long graphicSpecId;
}
