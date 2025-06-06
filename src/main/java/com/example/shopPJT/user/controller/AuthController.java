package com.example.shopPJT.user.controller;

import com.example.shopPJT.user.dto.JoinSellerDto;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 API")
public class AuthController {
    private final UserService userService;
    @Value("${app.client-url}")
    private String clientUrl;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 기본 회원가입: 일반 사용자
    @PostMapping("/join")
    @Operation(summary = "일반 사용자 회원가입")
    public ResponseEntity<?> join(@ModelAttribute JoinUserDto joinUserDto) {
        if(userService.join(joinUserDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 기본 회원가입: 판매자 유형
    @PostMapping("/join/seller")
    @Operation(summary = "판매자 회원가입", description = "추가적으로 필요한 정보 입력")
    public ResponseEntity<?> joinSeller(@ModelAttribute JoinSellerDto joinSellerDto) {
        if(userService.joinSeller(joinSellerDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 이메일 중복확인
    @GetMapping("/dup/{email}")
    @Operation(summary = "이메일 중복검사", description = "중복된 이메일은 사용 불가")
    public ResponseEntity<?> isDupEmail(@PathVariable String email) {
        userService.isExistUser(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh") // Refresh Token을 이용하여 두 종류의 토큰 모두 재발급
    @Operation(summary = "토큰 모두 재발급", description = "Refresh Token을 이용, 두 종류의 토큰 모두 재발급")
    public ResponseEntity<?> tokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] newCookie = userService.refreshToken(request.getCookies());
        if(newCookie == null) {
            // 토큰 재발급 실패 시 로그인 화면으로 리다이렉션
            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, clientUrl + "/auth/login").build();
        }
        response.addCookie(newCookie[0]);
        response.addCookie(newCookie[1]);
        return ResponseEntity.ok().build();
    }


}
