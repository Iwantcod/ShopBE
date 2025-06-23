package com.example.shopPJT.recommendedUsage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class RecommendedUsage {
    @Id @GeneratedValue @Column(name = "RECOMMENDED_USAGE_ID")
    private Integer recommendedUsageId;
    @Column(nullable = false, unique = true)
    private String usageName;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDeleted = Boolean.FALSE;

    public void setUsageName(String usageName) {
        this.usageName = usageName;
    }
}
