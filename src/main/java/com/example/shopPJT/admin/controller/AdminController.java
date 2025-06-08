package com.example.shopPJT.admin.controller;

import com.example.shopPJT.benchmark.dto.ReqBenchMarkDto;
import com.example.shopPJT.benchmark.service.BenchMarkService;
import com.example.shopPJT.businessInfo.service.BusinessInfoService;
import com.example.shopPJT.product.dto.ReqCategoryDto;
import com.example.shopPJT.product.service.CategoryService;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.service.ProductSpecServiceFactory;
import com.example.shopPJT.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "관리자 API", description = "요청에 담긴 jwt의 내부 권한이 ADMIN인 경우에만 응답")
public class AdminController {
    private final UserService userService;
    private final BusinessInfoService businessInfoService;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;
    private final ProductSpecServiceFactory productSpecServiceFactory;
    private final BenchMarkService benchMarkService;

    @Autowired
    public AdminController(UserService userService, BusinessInfoService businessInfoService, CategoryService categoryService, ProductSpecServiceFactory productSpecServiceFactory, ObjectMapper objectMapper, BenchMarkService benchMarkService) {
        this.userService = userService;
        this.businessInfoService = businessInfoService;
        this.categoryService = categoryService;
        this.productSpecServiceFactory = productSpecServiceFactory;
        this.objectMapper = objectMapper;
        this.benchMarkService = benchMarkService;
    }

    @GetMapping("/check-on-approval/{startOffset}")
    @Operation(summary = "판매자 권한 승인을 기다리는 판매자 목록 조회(페이징)")
    public ResponseEntity<?> checkOnApproval(@PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(businessInfoService.getWaitingApproveSellerList(startOffset));
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

    @PatchMapping("/disapprove-seller/{userId}")
    @Operation(summary = "특정 판매자 권한 회수")
    public ResponseEntity<?> disapproveSellerAuth(@PathVariable Long userId) {
        if(businessInfoService.disapproveSellAuth(userId)) {
            if(userService.revokeSellerAuth(userId)) {
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

    @PostMapping("/spec/{categoryName}")
    @Operation(summary = "해당 카테고리의 모델 스펙 정보 추가") // 요청값을 @ModelAttribute로 바인딩할 때 일단 타입 T(제네릭)로 캡처
    public ResponseEntity<?> addSpec(@PathVariable String categoryName, @RequestBody Map<String, Object> requesMap) {
        Class<? extends ModelNameDto> dtoClass = productSpecServiceFactory.getDtoClass(categoryName);
        if(dtoClass == null) {
            return ResponseEntity.badRequest().build();
        }
        productSpecServiceFactory.getStrategy(categoryName.toLowerCase()).createSpec(objectMapper.convertValue(requesMap, dtoClass));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/spec/{categoryName}")
    @Operation(summary = "해당 카테고리의 모델 스펙 정보 수정")
    public ResponseEntity<?> updateSpec(@PathVariable String categoryName, @RequestBody Map<String, Object> requesMap) {
        Class<? extends ModelNameDto> dtoClass = productSpecServiceFactory.getDtoClass(categoryName);
        if(dtoClass == null) {
            return ResponseEntity.badRequest().build();
        }
        productSpecServiceFactory.getStrategy(categoryName.toLowerCase()).updateSpec(objectMapper.convertValue(requesMap, dtoClass));
        return ResponseEntity.ok().build();
    }


    @PostMapping("/benchmark")
    @Operation(summary = "벤치마크 정보 추가")
    public ResponseEntity<?> addBenchMark(@RequestBody ReqBenchMarkDto reqBenchMarkDto) {
        benchMarkService.addBenchMark(reqBenchMarkDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/benchmark/{benchMarkId}")
    @Operation(summary = "벤치마크 정보 수정", description = "수치 값만 수정할 수 있습니다.")
    public ResponseEntity<?> updateBenchMark(@PathVariable Long benchMarkId, @RequestBody ReqBenchMarkDto reqBenchMarkDto) {
        benchMarkService.updateBenchMark(benchMarkId, reqBenchMarkDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/benchmark/{benchMarkId}")
    @Operation(summary = "벤치마크 정보 삭제")
    public ResponseEntity<?> deleteBenchMark(@PathVariable Long benchMarkId) {
        benchMarkService.deleteBenchMark(benchMarkId);
        return ResponseEntity.ok().build();
    }
}