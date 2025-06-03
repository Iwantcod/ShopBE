package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CoolerSpec extends Spec {
    @Id @GeneratedValue @Column(name = "COOLERSPEC_ID")
    private Long id;
    @Column(nullable = false)
    private Integer size;
    @Column(nullable = false)
    private Integer fanSpeed;
    @Column(nullable = false)
    private Integer noise;

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setFanSpeed(Integer fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
    }
}
