package com.example.shopPJT.benchmark.entity;

import com.example.shopPJT.productSpec.entity.CpuSpec;
import com.example.shopPJT.productSpec.entity.GraphicSpec;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BenchMark {
    @Id @GeneratedValue @Column(name = "BENCHMARK_ID")
    private Long id;

    @JoinColumn(name = "CPUSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CpuSpec cpuSpec;

    @JoinColumn(name = "GRAPHICSPEC_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GraphicSpec graphicSpec;

    private Integer avgFrame1;
    private Integer avgFrame2;
    private Integer avgFrame3;

    public void setCpuSpec(CpuSpec cpuSpec) {
        this.cpuSpec = cpuSpec;
    }

    public void setGraphicSpec(GraphicSpec graphicSpec) {
        this.graphicSpec = graphicSpec;
    }

    public void setAvgFrame1(Integer avgFrame1) {
        this.avgFrame1 = avgFrame1;
    }

    public void setAvgFrame2(Integer avgFrame2) {
        this.avgFrame2 = avgFrame2;
    }

    public void setAvgFrame3(Integer avgFrame3) {
        this.avgFrame3 = avgFrame3;
    }
}
