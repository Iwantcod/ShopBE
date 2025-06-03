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
    @Column(nullable = false)
    private String chipSetType;
    @Column(nullable = false)
    private String cpuSocket;
    @Column(nullable = false)
    private Integer mosFet;

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
