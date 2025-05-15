package com.example.shopPJT.productSpec.service;

import com.example.shopPJT.productSpec.dto.ModelNameDto;

import java.util.List;

public interface ProductSpecServiceStrategy<T> {
    String getSpecType(); // 반환값을 반드시 '영어 소문자'로 고정할 것: ex) CategoryName.CPU.toString().toLowerCase();

    T getSpecById(Long specId);
    List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset);
    List<ModelNameDto> getSpecListPaging(Integer startOffset);
    void createSpec(T specDto);

    void updateSpec(T specDto);
    boolean isExist(Long specId);
}