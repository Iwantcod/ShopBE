package com.example.shopPJT.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ResReviewDto {
    private Long reviewId;
    private Long userId;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private String materializedPath;
}
