package com.example.shopPJT.admin.controller;

import com.example.shopPJT.businessInfo.service.BusinessInfoService;
import com.example.shopPJT.product.dto.ReqCategoryDto;
import com.example.shopPJT.product.service.CategoryService;
import com.example.shopPJT.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "관리자 권한", description = "요청에 담긴 jwt의 내부 권한이 ADMIN인 경우에만 응답")
public class AdminController {
    private final UserService userService;
    private final BusinessInfoService businessInfoService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(UserService userService, BusinessInfoService businessInfoService, CategoryService categoryService) {
        this.userService = userService;
        this.businessInfoService = businessInfoService;
        this.categoryService = categoryService;
    }


    // 특정 회원의 판매자 권한 승인(BusinessInfo 테이블), 권한(Role)을 판매자로 업데이트(Users 테이블)
    @PatchMapping("/approve-seller/{userId}")
    @Operation(summary = "특정 회원의 권한을 판매자로 변경")
    public ResponseEntity<?> approveSellerAuth(@PathVariable Long userId) {
        if(businessInfoService.approveSellAuth(userId)) {
            if(userService.grantSellerAuth(userId)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/category")
    @Operation(summary = "특정 카테고리의 이름 변경")
    public ResponseEntity<?> updateCategory(@ModelAttribute ReqCategoryDto reqCategoryDto) {
        if(categoryService.updateCategory(reqCategoryDto)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/category")
    @Operation(summary = "특정 카테고리 제거", description = "가능한 사용 X")
    public ResponseEntity<?> deleteCategory(@ModelAttribute ReqCategoryDto reqCategoryDto) {
        categoryService.deleteCategory(reqCategoryDto);
        return ResponseEntity.ok().build();
    }
}