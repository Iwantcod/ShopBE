package com.example.shopPJT.product.entity;

import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class Product {
    @Id @GeneratedValue @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;

    @JoinColumn(name = "CATEGORY_ID")
    @ManyToOne(fetch = FetchType.LAZY)

    private Category category;
    private Long logicalFK;
    private int price;
    private int inventory;
    private LocalDate createdAt;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String productImageUrl;
    private String descriptionImageUrl;
    private boolean isDeleted = false;

    public void setName(String name) {
        this.name = name;
    }

    public void setLogicalFK(Long logicalFK) {
        this.logicalFK = logicalFK;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescriptionImageUrl(String descriptionImageUrl) {
        this.descriptionImageUrl = descriptionImageUrl;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
