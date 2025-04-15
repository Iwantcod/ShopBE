package com.example.shopPJT.user.controller;

import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.dto.UpdateUserDto;
import com.example.shopPJT.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Value("${app.client-url}")
    private String clientUrl;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/refresh") // Refresh Token을 이용하여 두 종류의 토큰 모두 재발급
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

    @PatchMapping
    public ResponseEntity<?> updateUser(@ModelAttribute UpdateUserDto updateUserDto) {
        if(userService.updateUser(updateUserDto)) {
//            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, clientUrl + "/auth/login").build();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo() {
        if(userService.getMyInfo() != null) {
            return ResponseEntity.ok().body(userService.getMyInfo());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfoById(@PathVariable("userId") Long userId) {
        if(userService.getUserInfoById(userId) != null) {
            return ResponseEntity.ok().body(userService.getUserInfoById(userId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
