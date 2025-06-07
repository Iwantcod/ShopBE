package com.example.shopPJT.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApplicationError {
    // 에러 종류
    // 접근 권한 없는 경우: UNAUTHORIZED
    // 찾을 수 없는 경우: NOT_FOUND
    // 요청이 데이터베이스 혹은 스토리지에서 수행 실패 시: INTERNAL_SERVER_ERROR

    // 인증 및 인가 관련
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청의 토큰에 Role 정보가 존재하지 않습니다."),
    USERID_NOT_FOUND(HttpStatus.NOT_FOUND, "요청의 토큰에 회원 식별자가 존재하지 않습니다."),
    ACCESS_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "요청에서 Refresh Token이 포함된 쿠키를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.NOT_FOUND, "Refresh Token이 유효하지 않습니다."),
    REFRESH_TOKEN_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류로 인해 Refresh Token 정보를 갱신하지 못했습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저네임(닉네임)입니다."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
    WRONG_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),

    // 데이터 관련
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    PRODUCTID_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 식별자가 존재하지 않습니다."),
    PRODUCT_DELETED(HttpStatus.NOT_FOUND, "삭제된 상품입니다."),
    BUSINESSINFO_NOT_FOUND(HttpStatus.NOT_FOUND, "사업자 등록정보를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    USER_DELETED(HttpStatus.NOT_FOUND, "탈퇴한 회원입니다."),
    PRODUCTSPEC_NOT_FOUND(HttpStatus.NOT_FOUND, "제품 모델 정보를 찾을 수 없습니다."),
    BENCHMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "벤치마크 정보를 찾을 수 없습니다."),
    PRODUCT_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "재고 수량은 0 이상이어야 합니다."),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니가 존재하지 않습니다."),
    CARTID_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니 식별자가 존재하지 않습니다."),
    CART_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "장바구니 수량은 1 이상이어야 합니다."),
    PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고 수량이 부족합니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보가 존재하지 않습니다."),
    ORDER_REQUEST_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 요청입니다."),
    ORDER_CANNOT_DELETE(HttpStatus.BAD_REQUEST, "완료되지 않은 주문 정보는 삭제할 수 없습니다."),
    ORDER_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "준비 중인 주문만 취소할 수 있습니다."),
    PARENT_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "부모로 지정한 리뷰가 존재하지 않습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),


    // 데이터베이스 및 스토리지 관련
    USER_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 회원 정보를 삭제하지 못했습니다."),
    PRODUCT_IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 상품 대표 이미지 업로드를 실패했습니다."),
    PRODUCT_DESCRIPTION_IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 상품 상세 페이지 이미지 업로드를 실패했습니다."),
    PRODUCT_IMAGE_REMOVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 상품 대표 이미지 제거를 실패했습니다."),
    PRODUCT_DESCRIPTION_IMAGE_REMOVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 상품 상세 페이지 이미지 제거에 실패했습니다."),
    IMAGE_REMOVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 이미지 제거에 실패했습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    IMAGE_LOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 이미지를 불러올 수 없습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;
    ApplicationError(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
