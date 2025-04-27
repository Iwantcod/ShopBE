package com.example.shopPJT.productSpec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class CaseSpec extends Spec {
    @Id @GeneratedValue @Column(name = "CASESPEC_ID")
    private Long id;
    @Column(nullable = false)
    private Integer size;
    @Column(nullable = false)
    private Integer innerSpace;

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setInnerSpace(Integer innerSpace) {
        this.innerSpace = innerSpace;
    }
}
