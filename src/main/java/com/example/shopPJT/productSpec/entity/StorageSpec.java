package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class StorageSpec extends Spec {
    @Id @GeneratedValue @Column(name = "STORAGESPEC_ID")
    private Long id;
    @Column(nullable = false)
    private String formFactorType;
    @Column(nullable = false)
    private Integer volume;
    @Column(nullable = false)
    private Integer fanSpeed; // 디스크 팬 속도
    @Column(nullable = false)
    private String groups; // 스토리지 분류: HDD, SSD, NVM

    public void setFormFactorType(String formFactorType) {
        this.formFactorType = formFactorType;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public void setFanSpeed(Integer fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
