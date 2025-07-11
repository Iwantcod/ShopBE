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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private ResCategoryDto toDto(Category category) {
        ResCategoryDto resCategoryDto = new ResCategoryDto();
        resCategoryDto.setCategoryId(category.getId());
        resCategoryDto.setCategoryName(category.getName());
        return resCategoryDto;
    }

    @Transactional(readOnly = true) // 카테고리 식별자로 카테고리 정보 조회
    public ResCategoryDto getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND));
        return toDto(category);
    }

    @Transactional(readOnly = true) // 카테고리 이름으로 카테고리 정보 조회
    public ResCategoryDto getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() ->
                new ApplicationException(ApplicationError.CATEGORY_NOT_FOUND));
        return toDto(category);
    }

    @Transactional(readOnly = true)
    public List<ResCategoryDto> getAllCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(this::toDto).toList();
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
    public void deleteCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public void createCategory(ReqCategoryDto reqCategoryDto) {
        Category category = new Category();
        category.setName(reqCategoryDto.getCategoryName());
        categoryRepository.save(category);
    }
}
