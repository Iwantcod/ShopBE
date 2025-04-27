package com.example.shopPJT.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // ApplicationError enum을 이용하여 실제 에러 객체를 생성하는 클래스
public class ApplicationException extends RuntimeException {
    private final ApplicationError applicationError;
    public ApplicationException(ApplicationError applicationError) {
        this.applicationError = applicationError;
    }

    public HttpStatus getHttpStatus() {
        // ApplicationError의 Http 상태코드를 불러온다.
        return applicationError.getHttpStatus();
    }
    public String getMessage() {
        // ApplicationError의 상세 에러 메시지를 불러온다.
        return applicationError.getMessage();
    }
}
