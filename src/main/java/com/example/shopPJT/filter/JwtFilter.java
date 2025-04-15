package com.example.shopPJT.filter;


import com.example.shopPJT.user.details.JwtUserDetails;
import com.example.shopPJT.user.dto.AuthDto;
import com.example.shopPJT.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // jwt 검증을 위한 필터. 해당 필터에서 인증이 완료되지 않으면 '로그인 필터'로 요청이 전달됩니다.
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override // access token을 검증하는 메소드. refresh token에 대한 검증 요청은 AuthController가 처리
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {
        String accessToken = null;

        // 요청에 쿠키가 존재하는지 확인
        if(request.getCookies() != null) {
            // 쿠키에서 access token을 찾는 과정
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("access_token")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        // access token을 발견하지 못한 경우, 다음 필터로 요청을 넘긴다.
        if(accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰이 만료된 경우
        if(jwtUtil.isExpired(accessToken)) {
            filterChain.doFilter(request, response);
//            response.sendRedirect(request.getContextPath() + "/api/user/refresh"); // refresh token을 이용한 토큰 재발급 요청을 하도록 리다이렉션 응답
            return;
        }

        // 토큰에서 유저 식별자와 권한 정보 추출
        Long userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // 추출한 정보를 AuthDto에 담는다.
        AuthDto authDto = new AuthDto();
        authDto.setUserId(userId);
        authDto.setRoleType(role);
        authDto.setPassword("tmpPW");

        // AuthDto를 JwtUserDetails에 담아 인증 토큰을 생성하기 위한 준비물을 만든다.
        JwtUserDetails jwtUserDetails = new JwtUserDetails(authDto);
        // 인증토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());

        // Security Context Holder 임시 세션에 인증토큰 정보를 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}