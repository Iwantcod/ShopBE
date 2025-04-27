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
    @Column(nullable = false)
    private String groups; // 메모리 분류
    @Column(nullable = false)
    private Integer cl;
    @Column(nullable = false)
    private Integer volume; // 메모리 크기(용량)

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public void setCl(Integer cl) {
        this.cl = cl;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
