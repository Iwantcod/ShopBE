package com.example.shopPJT.orderItems.entity;

import com.example.shopPJT.order.entity.Order;
import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@org.hibernate.annotations.Check(constraints = "quantity >= 1")
public class OrderItems {
    @Id @GeneratedValue @Column(name = "ORDERITEMS_ID")
    private Long id;

    @JoinColumn(name = "ORDER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder
    public OrderItems(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.category = product.getCategory();
        this.quantity = quantity;
        this.user = order.getUser();
    }
}
