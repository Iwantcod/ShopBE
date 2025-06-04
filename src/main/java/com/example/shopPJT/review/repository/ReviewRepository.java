package com.example.shopPJT.review.repository;

import com.example.shopPJT.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
