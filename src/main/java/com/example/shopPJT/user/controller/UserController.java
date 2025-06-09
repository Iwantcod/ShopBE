package com.example.shopPJT.user.controller;

import com.example.shopPJT.product.service.ProductService;
import com.example.shopPJT.user.dto.JoinUserDto;
import com.example.shopPJT.user.dto.ResUserDto;
import com.example.shopPJT.user.dto.UpdateUserDto;
import com.example.shopPJT.user.entity.RoleType;
import com.example.shopPJT.user.service.UserService;
import com.example.shopPJT.util.AuthUtil;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "회원 API")
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    @Value("${app.client-url}")
    private String clientUrl;
    @Autowired
    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }


    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 자기 자신의 User 테이블 수정
    @Operation(summary = "자신의 회원 정보 수정", description = "jwt의 userId 정보에 해당하는 회원의 정보 수정")
    public ResponseEntity<Void> updateUser(@Valid @ModelAttribute UpdateUserDto updateUserDto) {
        if(userService.updateUser(updateUserDto)) {
//            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, clientUrl + "/auth/login").build();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/my-info") // 자신의 정보 확인(JWT 내부의 유저 식별자로 User 테이블 조회)
    @Operation(summary = "자신의 회원 정보 조회", description = "jwt의 userId 정보에 해당하는 회원의 정보 조회")
    public ResponseEntity<ResUserDto> getMyInfo() {
        ResUserDto resUserDto = userService.getMyInfo();
        if(resUserDto != null) {
            return ResponseEntity.ok().body(resUserDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}") // 회원 식별자로 User 테이블 조회
    @Operation(summary = "식별자로 특정 회원 정보 조회")
    public ResponseEntity<ResUserDto> getUserInfoById(@PathVariable("userId") Long userId) {
        ResUserDto resUserDto = userService.getUserInfoById(userId);
        if(resUserDto != null) {
            return ResponseEntity.ok().body(resUserDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{emailKey}") // 회원 이메일로 User 테이블 조회
    @Operation(summary = "이메일 주소 키워드로 특정 회원 정보 조회", description = "관리자 정보는 조회되지 않습니다.")
    public ResponseEntity<List<ResUserDto>> getUserInfoByEmail(@PathVariable("emailKey") String key) {
        return ResponseEntity.ok().body(userService.getUserInfoByEmail(key));
    }

    @GetMapping("/username/user/{userNameKey}") // 유저네임 키워드로 일반 회원 조회
    @Operation(summary = "유저네임 키워드로 일반 회원 정보 조회")
    public ResponseEntity<List<ResUserDto>> getUserInfoByUsername(@PathVariable("userNameKey") String key) {
        return ResponseEntity.ok().body(userService.getUserListByUsernameKey(key));
    }

    @GetMapping("/username/seller/{userNameKey}") // 유저네임 키워드로 판매자 조회
    @Operation(summary = "유저네임(상호명) 키워드로 판매자 회원 정보 조회")
    public ResponseEntity<List<ResUserDto>> getSellerInfoByUsername(@PathVariable("userNameKey") String key) {
        return ResponseEntity.ok().body(userService.getSellerListByUsernameKey(key));
    }

    @PatchMapping("/off") // JWT 내부의 회원 식별자에 해당하는 회원을 '삭제(탈퇴)' 처리
    @Operation(summary = "자신의 계정 탈퇴 처리", description = "jwt 내부의 회원 식별자에 해당하는 회원을 삭제(탈퇴) 처리")
    public ResponseEntity<Void> userDeleteTrue() {
        Long userId = AuthUtil.getSecurityContextUserId();
        String role = AuthUtil.getCurrentUserAuthority();

        // 탈퇴하려는 회원이 '판매자'인 경우, 이 회원이 업로드한 모든 상품을 '삭제처리'
        if(role != null) {
            if(role.equals(RoleType.ROLE_SELLER.toString())) {
                productService.setAllProductDeleteTrueByUserId(userId);
            }
        }

        if(userService.userDeleteTrue(userId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
