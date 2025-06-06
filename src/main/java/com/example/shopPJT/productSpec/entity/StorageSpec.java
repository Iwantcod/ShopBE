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
    private String formFactorType;
    private Integer volume;
    private Integer fanSpeed; // 디스크 팬 속도
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
