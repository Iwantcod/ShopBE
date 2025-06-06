package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CpuSpec extends Spec {
    @Id @GeneratedValue @Column(name = "CPUSPEC_ID")
    private Long id;
    private String coreNum;
    private String threadNum;
    private Integer l3Cache;
    private Integer boostClock;
    private Integer processSize;

    public void setCoreNum(String coreNum) {
        this.coreNum = coreNum;
    }

    public void setProcessSize(Integer processSize) {
        this.processSize = processSize;
    }

    public void setThreadNum(String threadNum) {
        this.threadNum = threadNum;
    }

    public void setL3Cache(Integer l3Cache) {
        this.l3Cache = l3Cache;
    }

    public void setBoostClock(Integer boostClock) {
        this.boostClock = boostClock;
    }
}
