package com.example.shopPJT.user.controller;

import com.example.shopPJT.user.dto.JoinCompleteDto;
import com.example.shopPJT.user.dto.JoinSellerDto;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.service.UserService;
import com.example.shopPJT.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증 API")
public class AuthController {
    private final UserService userService;
    @Value("${app.client-url}")
    private String clientUrl;
    private JwtUtil jwtUtil;
    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 기본 회원가입: 일반 사용자
    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "일반 사용자 회원가입", description = "일반 사용자의 유저네임은 중복될 수 없습니다.")
    public ResponseEntity<Void> join(@ModelAttribute @Valid JoinUserDto joinUserDto) {
        if(userService.join(joinUserDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 소셜 가입자 추가 정보 입력
    @PostMapping(value = "/join-complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "OAuth2 가입자 추가 정보 저장")
    public ResponseEntity<Void> joinComplete(@ModelAttribute @Valid JoinCompleteDto joinCompleteDto, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("incompleteUserId");
        request.getSession().removeAttribute("incompleteUserId");
        userService.completeJoin(userId, joinCompleteDto);
        return ResponseEntity.ok().build();
    }

    // 기본 회원가입: 판매자 유형
    @PostMapping(value = "/join/seller", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "판매자 회원가입", description = "사업자명은 판매자의 유저네임이 됩니다. 판매자의 경우 유저네임 중복을 허용합니다.")
    public ResponseEntity<Void> joinSeller(@ModelAttribute @Valid JoinSellerDto joinSellerDto) {
        if(userService.joinSeller(joinSellerDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 이메일 중복확인
    @GetMapping("/dup-email/{email}")
    @Operation(summary = "이메일 중복검사", description = "중복된 이메일은 사용 불가")
    public ResponseEntity<Void> isDupEmail(@PathVariable String email) {
        userService.isExistEmail(email);
        return ResponseEntity.ok().build();
    }

    // 유저네임 중복 확인
    @GetMapping("/dup-username/{username}")
    @Operation(summary = "유저네임 중복검사", description = "일반 사용자의 유저네임은 중복 불가")
    public ResponseEntity<Void> isDupUsername(@PathVariable String username) {
        userService.isExistUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh") // Refresh Token을 이용하여 두 종류의 토큰 모두 재발급
    @Operation(summary = "토큰 모두 재발급", description = "Refresh Token을 이용, 두 종류의 토큰 모두 재발급")
    public ResponseEntity<Void> tokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        ResponseCookie[] newCookie = userService.refreshToken(request.getCookies());
        if(newCookie == null) {
            // 토큰 재발급 실패 시 로그인 화면으로 리다이렉션
            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, clientUrl + "/auth/login").build();
        }
        for(ResponseCookie cookie : newCookie) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout") // 로그아웃 메소드, 쿠키의 수명을 0으로 설정하여 클라이언트로 덮어씌움.
    public void logout(HttpServletResponse res) {
//        res.addCookie(jwtUtil.createMax0AccessToken());
//        res.addCookie(jwtUtil.createMax0RefreshToken());
        ResponseCookie access = jwtUtil.createMax0AccessToken();
        ResponseCookie refresh = jwtUtil.createMax0RefreshToken();
        res.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        res.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
    }
}
