package com.example.shopPJT.global.exception;

import com.example.shopPJT.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class) // 커스텀한 에러 발생 시 처리 로직
    public ResponseEntity<?> applicationException(ApplicationException e) {
        // 현재 요청 정보 load
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes != null ? requestAttributes.getRequest() : null;

        // 현재 요청에 담긴 jwt에서 회원 정보 추출
        Long userId = AuthUtil.getSecurityContextUserId(); // 식별자
        String userRole = AuthUtil.getCurrentUserAuthority(); // role

        if(request != null) {
            // 요청 url 정보 load
            String url = request.getRequestURL().toString();

            // 요청 파라메터를 모두 읽어서 문자열로 변환
            StringBuilder params = new StringBuilder();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                params.append(paramName).append("=").append(paramValue).append("&");
            }

            // 에러 로그 메시지 생성
            log.error("[Application Exception] User ID: {}, User Role: {}, URL: {}, Params: {}, Error Message: {}, Error Code: {}",
                    userId,
                    userRole,
                    url,
                    (!params.isEmpty() ? params.substring(0, params.length() - 1) : ""),
                    e.getMessage(),
                    e.getHttpStatus());
        } else {
            // 요청 정보를 가져올 수 없는 경우의 에러 로그
            log.error("[Application Exception] User ID: {}, User Role: {}, Error Message: {}, Error Code: {}", userId, userRole, e.getMessage(), e.getHttpStatus());
        }

        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 유효하지 않은 값이 입력된 요청을 받았을 때 발생하는 에러
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 하나의 요청에 여러 개의 '유효하지 않은 값 에러'가 동시에 발생할 수 있다.
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();
        for (ObjectError objectError : objectErrors) {
            errors.add(objectError.getDefaultMessage());
        }
        String error = String.join("\n", errors);
        log.error("{} : {}", e.getCause(), e.getMessage());


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PessimisticLockingFailureException.class) // 비관적 락 대기 timeout 발생 시 예외 처리
    public ResponseEntity<?> pessimisticLockingFailureException(PessimisticLockingFailureException e) {
        // 현재 요청 정보 load
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes != null ? requestAttributes.getRequest() : null;

        // 현재 요청에 담긴 jwt에서 회원 정보 추출
        Long userId = AuthUtil.getSecurityContextUserId(); // 식별자
        String userRole = AuthUtil.getCurrentUserAuthority(); // role

        if(request != null) {
            // 요청 url 정보 load
            String url = request.getRequestURL().toString();

            // 요청 파라메터를 모두 읽어서 문자열로 변환
            StringBuilder params = new StringBuilder();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                params.append(paramName).append("=").append(paramValue).append("&");
            }

            // 에러 로그 메시지 생성
            log.error("[Lock Timeout] User ID: {}, User Role: {}, URL: {}, Params: {}, Exception: {}",
                    userId,
                    userRole,
                    url,
                    (!params.isEmpty() ? params.substring(0, params.length() - 1) : ""),
                    e.getMessage());
        } else {
            // 요청 정보를 가져올 수 없는 경우의 에러 로그
            log.error("[Lock Timeout] User ID: {}, User Role: {}, Exception: {}", userId, userRole, e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lock Wait Timeout Error: 해당 요청을 수행하기 위한 락 획득에 실패했습니다.");
    }


    @ExceptionHandler(Exception.class) // 예상치 못한 에러 발생 시 대처를 위한 처리 로직
    public ResponseEntity<?> exception(Exception e) {
        // 현재 요청 정보 load
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes != null ? requestAttributes.getRequest() : null;

        // 현재 요청에 담긴 jwt에서 회원 정보 추출
        Long userId = AuthUtil.getSecurityContextUserId(); // 식별자
        String userRole = AuthUtil.getCurrentUserAuthority(); // role

        if(request != null) {
            // 요청 url 정보 load
            String url = request.getRequestURL().toString();

            // 요청 파라메터를 모두 읽어서 문자열로 변환
            StringBuilder params = new StringBuilder();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                params.append(paramName).append("=").append(paramValue).append("&");
            }

            // 에러 로그 메시지 생성
            log.error("[!!! Unexpected Exception !!!] User ID: {}, User Role: {}, URL: {}, Params: {}, Error Message: {}, Error Code: {}",
                    userId,
                    userRole,
                    url,
                    (!params.isEmpty() ? params.substring(0, params.length() - 1) : ""),
                    e.getMessage(),
                    e.getClass().getName());
        } else {
            // 요청 정보를 가져올 수 없는 경우의 에러 로그
            log.error("[!!! Unexpected Exception !!!] User ID: {}, User Role: {}, Error Message: {}, Error Code: {}",
                    userId, userRole, e.getMessage(), e.getClass().getName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
