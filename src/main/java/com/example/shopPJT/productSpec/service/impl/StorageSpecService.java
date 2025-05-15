package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.dto.StorageSpecDto;
import com.example.shopPJT.productSpec.entity.StorageSpec;
import com.example.shopPJT.productSpec.repository.StorageSpecRepository;
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
public class StorageSpecService implements ProductSpecServiceStrategy<StorageSpecDto> {
    private final StorageSpecRepository storageSpecRepository;
    @Autowired
    public StorageSpecService(StorageSpecRepository storageSpecRepository) {
        this.storageSpecRepository = storageSpecRepository;
    }

    private StorageSpecDto toDto(StorageSpec storageSpec) {
        StorageSpecDto storageSpecDto = new StorageSpecDto();
        BeanUtils.copyProperties(storageSpec, storageSpecDto);
        return storageSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.STORAGE.toString().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public StorageSpecDto getSpecById(Long storageSpecId) { //
        StorageSpec storageSpec = storageSpecRepository.findById(storageSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(storageSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> storageSpecs = storageSpecRepository.searchByKeyword(pageable, modelName); //
        if(storageSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return storageSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> storageSpecs = storageSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(storageSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Storage Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return storageSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(StorageSpecDto storageSpecDto) { //
        // 관리자만 모델 스펙 정보 추가 가능
        if(!AuthUtil.getCurrentUserAuthority().equals("ROLE_ADMIN")) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        StorageSpec storageSpec = new StorageSpec(); //
        BeanUtils.copyProperties(storageSpecDto, storageSpec); //
        storageSpecRepository.save(storageSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(StorageSpecDto specDto) {
        StorageSpec storageSpec = storageSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getFanSpeed() != null) storageSpec.setFanSpeed(specDto.getFanSpeed());
        if(specDto.getVolume() != null) storageSpec.setVolume(specDto.getVolume());
        if(specDto.getManufacturer() != null) storageSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getModelName() != null) storageSpec.setModelName(specDto.getModelName());
        if(specDto.getFormFactorType() != null) storageSpec.setFormFactorType(specDto.getFormFactorType());
        if(specDto.getGroups() != null) storageSpec.setGroups(specDto.getGroups());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return storageSpecRepository.existsById(specId);
    }

}
