package com.example.shopPJT.review.controller;

import com.example.shopPJT.review.dto.ReqReviewDto;
import com.example.shopPJT.review.dto.ReqUpdateReviewDto;
import com.example.shopPJT.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> addReview(@ModelAttribute @Valid ReqReviewDto reqReviewDto) {
        reviewService.addReview(reqReviewDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId ,@ModelAttribute @Valid ReqUpdateReviewDto reqUpdateReviewDto) {
        reviewService.updateReview(reviewId,reqUpdateReviewDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
