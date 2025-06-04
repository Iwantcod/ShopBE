package com.example.shopPJT.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReqReviewDto {
    @NotNull
    private Long productId;
    private Long parentReviewId; // 부모 식별자 값은 null 가능
    @NotBlank
    private String comment;
}
