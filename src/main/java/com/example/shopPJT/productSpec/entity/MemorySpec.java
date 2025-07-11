package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class MemorySpec extends Spec {
    @Id @GeneratedValue @Column(name = "MEMORYSPEC_ID")
    private Long id;
    private String groups; // 메모리 분류
    private Integer cl;
    private Integer volume; // 메모리 크기(용량)
    private Integer speed; // 메모리 속도

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public void setCl(Integer cl) {
        this.cl = cl;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
}
