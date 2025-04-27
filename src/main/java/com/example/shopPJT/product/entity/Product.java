package com.example.shopPJT.product.entity;

import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        indexes = {
                @Index(name = "idx_product_id", columnList = "PRODUCT_ID")
        }
)
public class Product {
    @Id @GeneratedValue @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;

    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(nullable = false)
    private Long logicalFK;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int inventory;

    @CreationTimestamp
    private LocalDate createdAt;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String productImageUrl;
    private String descriptionImageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isDeleted = false;

    private int volume = 0; // 누적 판매량 저장하는 컬럼

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

    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
