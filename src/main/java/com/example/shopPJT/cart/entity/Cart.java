package com.example.shopPJT.cart.entity;

import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Entity
@Getter
@Table(
        indexes = {
                @Index(name = "idx_cart_userid", columnList = "USER_ID")
        }
)
@org.hibernate.annotations.Check(constraints = "quantity >= 0") // 수량 정보가 반드시 0 이상이도록 제한하는 DB레벨 제약사항
public class Cart {

    @Id @GeneratedValue @Column(name = "CART_ID")
    private Long id;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @PositiveOrZero // 애플리케이션 레벨 제약사항
    @Column(nullable = false)
    private Integer quantity;

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
