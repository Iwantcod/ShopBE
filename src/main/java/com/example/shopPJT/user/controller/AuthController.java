package com.example.shopPJT.user.controller;

import com.example.shopPJT.user.dto.JoinSellerDto;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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


}
