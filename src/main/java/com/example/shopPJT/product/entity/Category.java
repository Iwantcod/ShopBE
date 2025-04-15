package com.example.shopPJT.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Category {
    @Id @GeneratedValue @Column(name = "CATEGORY_ID")
    private Long id;
    @Column(nullable = false)
    private CategoryName name;

    public void setName(CategoryName name) {
        this.name = name;
    }
}
