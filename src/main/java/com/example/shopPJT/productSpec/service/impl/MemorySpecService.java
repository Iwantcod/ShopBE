package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.MemorySpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.MemorySpec;
import com.example.shopPJT.productSpec.repository.MemorySpecRepository;
import com.example.shopPJT.productSpec.service.ProductSpecServiceStrategy;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MemorySpecService implements ProductSpecServiceStrategy<MemorySpecDto> {
    private final MemorySpecRepository memorySpecRepository;
    @Autowired
    public MemorySpecService(MemorySpecRepository memorySpecRepository) {
        this.memorySpecRepository = memorySpecRepository;
    }

    private MemorySpecDto toDto(MemorySpec memorySpec) {
        MemorySpecDto memorySpecDto = new MemorySpecDto();
        BeanUtils.copyProperties(memorySpec, memorySpecDto);
        return memorySpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.MEMORY.toString().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public MemorySpecDto getSpecById(Long memorySpecId) { //
        MemorySpec memorySpec = memorySpecRepository.findById(memorySpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(memorySpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> memorySpecs = memorySpecRepository.searchByKeyword(pageable, modelName); //
        if(memorySpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return memorySpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> memorySpecs = memorySpecRepository.findAllByCreatedAtDesc(pageable); //
        if(memorySpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Memory Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return memorySpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(MemorySpecDto memorySpecDto) { //
        // 관리자만 모델 스펙 정보 추가 가능
        if(!AuthUtil.getCurrentUserAuthority().equals("ROLE_ADMIN")) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        MemorySpec memorySpec = new MemorySpec(); //
        BeanUtils.copyProperties(memorySpecDto, memorySpec); //
        memorySpecRepository.save(memorySpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(MemorySpecDto specDto) {
        MemorySpec memorySpec = memorySpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getCl() != null) memorySpec.setCl(specDto.getCl());
        if(specDto.getModelName() != null) memorySpec.setModelName(specDto.getModelName());
        if(specDto.getGroups() != null) memorySpec.setGroups(specDto.getGroups());
        if(specDto.getVolume() != null) memorySpec.setVolume(specDto.getVolume());
        if(specDto.getManufacturer() != null) memorySpec.setManufacturer(specDto.getManufacturer());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return memorySpecRepository.existsById(specId);
    }
}