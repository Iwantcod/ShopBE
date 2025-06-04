package com.example.shopPJT.review.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.orderItems.repository.OrderItemsRepository;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.review.dto.ReqReviewDto;
import com.example.shopPJT.review.dto.ReqUpdateReviewDto;
import com.example.shopPJT.review.entity.Review;
import com.example.shopPJT.review.repository.ReviewRepository;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository, OrderItemsRepository orderItemsRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Transactional
    public void addReview(ReqReviewDto reqReviewDto) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        // 상품 구매자만 리뷰 작성 가능
        if(orderItemsRepository.checkProductPurchase(reqReviewDto.getProductId(), userId) == 0) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ApplicationException(ApplicationError.USER_NOT_FOUND));
        Product product = productRepository.findById(reqReviewDto.getProductId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));


        Review review = Review.builder()
                .user(user)
                .product(product)
                .comment(reqReviewDto.getComment())
                .build();

        reviewRepository.save(review);

        // 추가하려는 것이 답변인 경우(부모 리뷰 존재하는 경우)
        if(reqReviewDto.getParentReviewId() != null) {
            String auth = AuthUtil.getCurrentUserAuthority();
            if(auth == null || auth.equals("USER")) {
                // 일반 유저는 리뷰에 대한 답변을 작성할 수 없다.
                throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
            }
            Review parent = reviewRepository.findById(reqReviewDto.getParentReviewId()).orElseThrow(() ->
                    new ApplicationException(ApplicationError.PARENT_REVIEW_NOT_FOUND));
            if(!parent.getProduct().getId().equals(review.getProduct().getId())) {
                // 부모 리뷰가 다른 상품의 리뷰인 경우, 예외 발생(즉, 잘못된 값 입력된 경우)
                throw new ApplicationException(ApplicationError.WRONG_REQUEST);
            }
            review.setParentReview(parent);
            review.setMaterializedPath(parent.getMaterializedPath() + "." + String.valueOf(review.getId())); // 리뷰 경로 저장
        } else {
            // 일반 리뷰인 경우
            review.setMaterializedPath(String.valueOf(review.getId())); // 리뷰 경로 저장
        }

    }

    // 해당 리뷰에 대한 접근 권한 검증 후 리뷰 엔티티 반환
    private Review vertificationReview(Long reviewId, Long userId) {
        if(reviewId == null) {
            throw new ApplicationException(ApplicationError.REVIEW_NOT_FOUND);
        }
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new ApplicationException(ApplicationError.REVIEW_NOT_FOUND));
    }

    @Transactional
    public void updateReview(Long reviewId, ReqUpdateReviewDto reqUpdateReviewDto) {
        Long userId = AuthUtil.getSecurityContextUserId();
        Review review = vertificationReview(reviewId, userId);
        if(!review.getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        review.setComment(reqUpdateReviewDto.getComment());
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        Review review = vertificationReview(reviewId, userId);
        review.setDeleted(true);
    }

}
