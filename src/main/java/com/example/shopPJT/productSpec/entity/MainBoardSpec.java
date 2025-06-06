package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class MainBoardSpec extends Spec {
    @Id @GeneratedValue @Column(name = "MAINBOARDSPEC_ID")
    private Long id;
    private String chipSetType;
    private String cpuSocket;
    private Integer mosFet;
    private String groups; // 사용 CPU 종류 분류
    private String modelGroups; // 메인보드 크기 분류

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public void setModelGroups(String modelGroups) {
        this.modelGroups = modelGroups;
    }

    public void setChipSetType(String chipSetType) {
        this.chipSetType = chipSetType;
    }

    public void setCpuSocket(String cpuSocket) {
        this.cpuSocket = cpuSocket;
    }

    public void setMosFet(Integer mosFet) {
        this.mosFet = mosFet;
    }
}
