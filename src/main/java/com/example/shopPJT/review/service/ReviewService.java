package com.example.shopPJT.review.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.orderItems.repository.OrderItemsRepository;
import com.example.shopPJT.product.dto.ResProductDto;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.review.dto.ReqReviewDto;
import com.example.shopPJT.review.dto.ReqUpdateReviewDto;
import com.example.shopPJT.review.dto.ResReviewDto;
import com.example.shopPJT.review.entity.Review;
import com.example.shopPJT.review.repository.ReviewRepository;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true) // 리뷰 작성 권한 확인: 유저의 주문 내역에 해당 상품이 존재하는지
    public void checkWritePermission(Long productId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        // 상품 구매자가 아닌 경우 예외 처리
        if(orderItemsRepository.checkProductPurchase(productId, userId) == 0) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
    }

    @Transactional(readOnly = true) // 판매자의 리뷰 답변 작성 권한 확인
    public void checkAnswerWritePermission(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        if(!product.getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
    }

    @Transactional
    public void addReview(ReqReviewDto reqReviewDto) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
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
            if(!parent.getProduct().getId().equals(review.getProduct().getId()) || !product.getUser().getId().equals(userId)) {
                // 부모 리뷰가 다른 상품의 리뷰인 경우, 예외 발생(즉, 잘못된 값 입력된 경우)
                // 다른 상품 리뷰에 답변을 작성하려는 요청인 경우 예외 발생(즉, 잘못된 값 입력된 경우)
                throw new ApplicationException(ApplicationError.WRONG_REQUEST);
            }
            review.setParentReview(parent);
            review.setMaterializedPath(parent.getMaterializedPath() + "." + String.valueOf(review.getId())); // 리뷰 경로 저장
        } else {
            // 일반 리뷰인 경우
            review.setMaterializedPath(String.valueOf(review.getId())); // 리뷰 경로 저장
        }
    }


    private ResReviewDto toDto(Review review) {
        ResReviewDto resReviewDto = new ResReviewDto();
        resReviewDto.setReviewId(review.getId());
        resReviewDto.setCreatedAt(review.getCreatedAt());
        resReviewDto.setMaterializedPath(review.getMaterializedPath());
        if(review.isDeleted()) {
            resReviewDto.setComment("삭제되었습니다.");
        } else {
            resReviewDto.setComment(review.getComment());
            resReviewDto.setUserId(review.getUser().getId());
            resReviewDto.setUsername(review.getUser().getUsername());
        }
        return resReviewDto;
    }
    @Transactional(readOnly = true)
    public List<ResReviewDto> getReviewsPaging(Long productId, Integer startOffset) {
        if(productId == null) {
            throw new ApplicationException(ApplicationError.PRODUCTID_NOT_FOUND);
        }
        if(startOffset == null) {
            startOffset = 0;
        }
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("materializedPath").ascending());
        Slice<Review> reviewList = reviewRepository.findByProductId(pageable, productId);
        if(reviewList.isEmpty()){
            throw new ApplicationException(ApplicationError.REVIEW_NOT_FOUND);
        }
        Slice<ResReviewDto> dtoPage = reviewList.map(this::toDto);
        return dtoPage.getContent();
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
