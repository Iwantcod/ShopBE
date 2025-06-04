package com.example.shopPJT.review.entity;

import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue @Column(name = "REVIEW_ID")
    private Long id;
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "PARENT_REVIEW_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Review parentReview; // 부모 리뷰 식별자 정보(대댓글인 경우에만 이 컬럼의 값을 가진다.)

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    private String materializedPath;

    @Builder
    public Review(Product product, User user, String comment) {
        this.product = product;
        this.user = user;
        this.comment = comment;
        this.isDeleted = false;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setParentReview(Review parentReview) {
        this.parentReview = parentReview;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setMaterializedPath(String materializedPath) {
        this.materializedPath = materializedPath;
    }
}
