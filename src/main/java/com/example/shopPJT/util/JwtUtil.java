package com.example.shopPJT.util;

import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Long expiration;
    private final SecretKey refreshKey;
    private final Long refreshExpiration;
    private final UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration
            , @Value("${jwt.refresh-secret}") String refreshSecret, @Value("${jwt.refresh-expiration}") Long refreshExpiration, UserRepository userRepository) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.refreshKey = new SecretKeySpec(refreshSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
        this.userRepository = userRepository;
    }

    // 토큰을 SecretKey로 파싱하여 Claims를 얻어내는 메소드
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 유저 식별자 추출
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    // 토큰에서 유저 권한 추출
    public String getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    // 토큰에서 토큰 식별자(uuid) 추출
    public String getId(String token) {
        Claims claims = parseToken(token);
        return claims.getId();
    }

    // Refresh 토큰에서 유저 식별자 추출
    public Long getRefreshUserId(String token) {
        Claims claims = parseRefreshToken(token);
        return claims.get("userId", Long.class);
    }

    // Refresh 토큰에서 유저 권한 추출
    public String getRefreshRole(String token) {
        Claims claims = parseRefreshToken(token);
        return claims.get("role", String.class);
    }

    // Refresh 토큰에서 토큰 식별자(uuid) 추출
    public String getRefreshId(String token) {
        Claims claims = parseRefreshToken(token);
        return claims.getId();
    }

    // 토큰 만료여부 검증
    public boolean isExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    // access token 발급
    public Cookie createAccessToken(Long userId, String role) {
        String access = Jwts.builder()
                .claim("userId", userId) // userId
                .claim("role", role)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        Cookie accessToken = new Cookie("access_token", access);
        accessToken.setHttpOnly(true); // JS에서의 접근 불가 옵션
        accessToken.setPath("/"); // 애플리케이션 모든 경로에서 서버로 전송
        accessToken.setMaxAge((int)(expiration/1000)); // 쿠키 유효시간: 30분(초 단위이므로 나누기 1천을 했습니다.)
        return accessToken;
    }

    // refresh token 발급
    public Cookie createRefreshToken(Long userId, String role) {
        String refresh = Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();

        Cookie refreshToken = new Cookie("refresh_token", refresh);
        refreshToken.setHttpOnly(true);
        refreshToken.setPath("/api/user/refresh"); // refresh token은 해당 경로에 대해서만 서버로 전송
        refreshToken.setMaxAge((int)(refreshExpiration/1000));
        return refreshToken;
    }

    // 특정 유저가 서버로 전송한 refresh token값이 해당 유저의 테이블의 refresh token 값과 일치하는지 검증
    public boolean isExistRefreshToken(Long userId, String refreshToken) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            return false;
        }

        return user.get().getRefreshToken().equals(refreshToken);
    }
}
