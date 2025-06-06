package com.example.shopPJT.review.controller;

import com.example.shopPJT.review.dto.ReqReviewDto;
import com.example.shopPJT.review.dto.ReqUpdateReviewDto;
import com.example.shopPJT.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@Tag(name = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(summary = "리뷰 작성", description = "parentReviewId는 필수 값이 아닙니다.")
    public ResponseEntity<?> addReview(@ModelAttribute @Valid ReqReviewDto reqReviewDto) {
        reviewService.addReview(reqReviewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/{startOffset}")
    @Operation(summary = "리뷰 조회", description = "계층 구조는 materializedPath 값으로 식별하시면 됩니다.")
    public ResponseEntity<?> getReview(@PathVariable Long productId, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(reviewService.getReviewsPaging(productId, startOffset));
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "자신이 작성한 리뷰만 수정할 수 있습니다.")
    public ResponseEntity<?> updateReview(@PathVariable("reviewId") Long reviewId ,@ModelAttribute @Valid ReqUpdateReviewDto reqUpdateReviewDto) {
        reviewService.updateReview(reviewId,reqUpdateReviewDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제 처리", description = "자신이 작성한 리뷰만 삭제할 수 있습니다.")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
