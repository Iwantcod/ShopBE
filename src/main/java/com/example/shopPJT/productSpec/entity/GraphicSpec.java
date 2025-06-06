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
    private String chipSetType;
    private String chipSetManufacturer;
    private String series;
    private Integer recommendPower;
    private Integer coreClock;
    private Integer boostClock;
    private Integer vram;
    private String groups;

    public void setGroups(String groups) {
        this.groups = groups;
    }

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
