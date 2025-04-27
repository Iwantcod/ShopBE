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
    @Column(nullable = false)
    private Integer coreNum;
    @Column(nullable = false)
    private Integer threadNum;
    @Column(nullable = false)
    private Integer l3Cache;
    @Column(nullable = false)
    private Integer boostClock;

    public void setCoreNum(Integer coreNum) {
        this.coreNum = coreNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public void setL3Cache(Integer l3Cache) {
        this.l3Cache = l3Cache;
    }

    public void setBoostClock(Integer boostClock) {
        this.boostClock = boostClock;
    }
}
