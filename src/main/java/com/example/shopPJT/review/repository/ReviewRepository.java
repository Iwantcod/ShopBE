package com.example.shopPJT.review.repository;

import com.example.shopPJT.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    Slice<Review> findByProductId(Pageable pageable, @Param("productId") Long productId);

}
