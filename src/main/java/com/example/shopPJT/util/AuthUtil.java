package com.example.shopPJT.util;

import com.example.shopPJT.user.details.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthUtil {
    private AuthUtil() {
        // 유틸 클래스이므로 인스턴스화 방지
    }

    // 현재 인증된 사용자의 userId를 반환
    // 인증 정보가 없거나 JwtUserDetails가 아니면 null을 반환
    public static Long getSecurityContextUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtUserDetails) {
            return ((JwtUserDetails) principal).getUserId();
        } else {
            return null;
        }
    }

}
