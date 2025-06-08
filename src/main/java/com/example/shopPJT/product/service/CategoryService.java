package com.example.shopPJT.product.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.dto.ReqCategoryDto;
import com.example.shopPJT.product.dto.ResCategoryDto;
import com.example.shopPJT.product.entity.Category;
import com.example.shopPJT.product.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true) // 카테고리 식별자로 카테고리 정보 조회
    public ResCategoryDto getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND));
        ResCategoryDto resCategoryDto = new ResCategoryDto();
        resCategoryDto.setCategoryId(categoryId);
        resCategoryDto.setCategoryName(category.getName());
        return resCategoryDto;
    }

    @Transactional(readOnly = true) // 카테고리 이름으로 카테고리 정보 조회
    public ResCategoryDto getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() ->
                new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND));
        ResCategoryDto resCategoryDto = new ResCategoryDto();
        resCategoryDto.setCategoryId(category.getId());
        resCategoryDto.setCategoryName(category.getName());
        return resCategoryDto;
    }


    @Transactional // 특정 카테고리의 이름 수정: 관리자 권한
    public boolean updateCategory(ReqCategoryDto reqCategoryDto) {
        Optional<Category> category = categoryRepository.findById(reqCategoryDto.getCategoryId());
        if(category.isPresent()) {
            category.get().setName(reqCategoryDto.getCategoryName());
            return true;
        } else {
            return false;
        }
    }

    @Transactional // 특정 카테고리 제거: 관리자 권한(가능한 사용하지 말것)
    public void deleteCategory(ReqCategoryDto reqCategoryDto) {
        categoryRepository.deleteById(reqCategoryDto.getCategoryId());
    }
}
