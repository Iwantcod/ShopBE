package com.example.shopPJT.product.controller;

import com.example.shopPJT.product.dto.ResCategoryDto;
import com.example.shopPJT.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}") // 카테고리 식별자로 카테고리 조회
    @Operation(summary = "카테고리 식별자로 카테고리 조회")
    public ResponseEntity<?> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        ResCategoryDto resCategoryDto = categoryService.getCategoryById(categoryId);
        if(resCategoryDto == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resCategoryDto);
        }
    }

    @GetMapping("/name/{categoryName}") // 카테고리 이름으로 카테고리 조회
    @Operation(summary = "카테고리 이름으로 카테고리 조회")
    public ResponseEntity<?> getCategoryByName(@PathVariable("categoryName") String categoryName) {
        ResCategoryDto resCategoryDto = categoryService.getCategoryByName(categoryName);
        if(resCategoryDto == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resCategoryDto);
        }

    }
}
