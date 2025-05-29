package com.example.shopPJT.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
    @Id @Column(name = "CATEGORY_ID")
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
}
