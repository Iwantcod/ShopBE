package com.example.shopPJT.cart.entity;

import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@org.hibernate.annotations.Check(constraints = "quantity >= 1") // 수량 정보가 반드시 0 이상이도록 제한하는 DB레벨 제약사항
public class Cart {

    @Id @GeneratedValue @Column(name = "CART_ID")
    private Long id;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.category = product.getCategory();
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
