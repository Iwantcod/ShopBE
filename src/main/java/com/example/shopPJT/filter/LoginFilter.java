package com.example.shopPJT.filter;

import com.example.shopPJT.user.details.JwtUserDetails;
import com.example.shopPJT.user.service.UserService;
import com.example.shopPJT.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @Value("${app.client-url}")
    private String clientUrl;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) auth.getPrincipal();

        Long userId = jwtUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = jwtUserDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String role = grantedAuthority.getAuthority();

//        response.addCookie(jwtUtil.createAccessToken(userId, role));
        ResponseCookie access = jwtUtil.createAccessToken(userId, role);
        response.addHeader(HttpHeaders.SET_COOKIE, access.toString());

        ResponseCookie refreshToken = jwtUtil.createRefreshToken(userId, role);
//        response.addCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        // 이미 로그인 시 유저 정보를 검증했으니, 이 메소드에서는 '업데이트' 쿼리만 호출한다.
        if(userService.setRefreshToken(userId, refreshToken) != 1) {
            // 업데이트 쿼리문에 영향을 받은 행의 개수가 1이 아닌 경우 에러 발생
            log.error("User ID Not Exist: {}", userId);
        }

        response.setHeader("Auth-Role", role); // 유저 권한
        response.setHeader("Auth-User-Id", String.valueOf(jwtUserDetails.getUserId())); // 유저 식별자
//        response.sendRedirect(clientUrl); // 홈페이지로 리다이렉션
    }

    @Override // 로그인 실패 시 수행되는 메소드
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {
        res.setStatus(401);
    }
}
