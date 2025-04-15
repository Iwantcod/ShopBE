package com.example.shopPJT.user.controller;

import com.example.shopPJT.user.dto.JoinSellerDto;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 기본 회원가입: 일반 사용자
    @PostMapping("/join")
    public ResponseEntity<?> join(@ModelAttribute JoinUserDto joinUserDto) {
        if(userService.join(joinUserDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 기본 회원가입: 판매자 유형
    @PostMapping("/join/seller")
    public ResponseEntity<?> joinSeller(@ModelAttribute JoinSellerDto joinSellerDto) {
        if(userService.joinSeller(joinSellerDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 이메일 중복확인
    @GetMapping("/dup/{email}")
    public ResponseEntity<?> isDupEmail(@PathVariable String email) {
        if(userService.isExistUser(email)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
