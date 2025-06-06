package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class PowerSpec extends Spec {
    @Id @GeneratedValue @Column(name = "POWERSPEC_ID")
    private Long id;
    private Integer ratedOutputPower;
    private String groups; // 파워 분류
    private String plusGrades; // 파워 효율성 등급

    public void setPlusGrades(String plusGrades) {
        this.plusGrades = plusGrades;
    }

    public void setRatedOutputPower(Integer ratedOutputPower) {
        this.ratedOutputPower = ratedOutputPower;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
}
