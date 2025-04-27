package com.example.shopPJT.util;

import com.example.shopPJT.user.details.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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


    // 현재 사용자의 '권한' 정보 반환(문자열)
    public static String getCurrentUserAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        // GrantedAuthority 의 Stream 중 첫 번째 요소를 꺼냅니다.
        return authentication.getAuthorities().stream()
                .findFirst()                      // Optional<GrantedAuthority>
                .map(GrantedAuthority::getAuthority)  // Optional<String>
                .orElse(null); // 권한 없으면 null 반환
    }

}
