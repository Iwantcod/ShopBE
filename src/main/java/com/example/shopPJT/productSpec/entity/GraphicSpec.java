package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class GraphicSpec extends Spec {
    @Id @GeneratedValue @Column(name = "GRAPHICSPEC_ID")
    private Long id;
    @Column(nullable = false)
    private String chipSetType;
    @Column(nullable = false)
    private String chipSetManufacturer;
    @Column(nullable = false)
    private String series;
    @Column(nullable = false)
    private Integer recommendPower;
    @Column(nullable = false)
    private Integer coreClock;
    @Column(nullable = false)
    private Integer boostClock;
    @Column(nullable = false)
    private Integer vram;

    public void setChipSetType(String chipSetType) {
        this.chipSetType = chipSetType;
    }

    public void setChipSetManufacturer(String chipSetManufacturer) {
        this.chipSetManufacturer = chipSetManufacturer;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setRecommendPower(Integer recommendPower) {
        this.recommendPower = recommendPower;
    }

    public void setCoreClock(Integer coreClock) {
        this.coreClock = coreClock;
    }

    public void setBoostClock(Integer boostClock) {
        this.boostClock = boostClock;
    }

    public void setVram(Integer vram) {
        this.vram = vram;
    }
}
