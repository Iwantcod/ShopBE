package com.example.shopPJT.product.controller;

import com.example.shopPJT.product.dto.ResCategoryDto;
import com.example.shopPJT.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@Tag(name = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{categoryId}") // 카테고리 식별자로 카테고리 조회
    @Operation(summary = "카테고리 식별자로 카테고리 조회")
    public ResponseEntity<ResCategoryDto> getCategoryById(@PathVariable("categoryId") Integer categoryId) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/name/{categoryName}") // 카테고리 이름으로 카테고리 조회
    @Operation(summary = "카테고리 이름으로 카테고리 조회")
    public ResponseEntity<ResCategoryDto> getCategoryByName(@PathVariable("categoryName") String categoryName) {
        return ResponseEntity.ok().body(categoryService.getCategoryByName(categoryName));
    }
}
