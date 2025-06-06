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
    private String groups;
    private String innerSpace;

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public void setInnerSpace(String innerSpace) {
        this.innerSpace = innerSpace;
    }
}
