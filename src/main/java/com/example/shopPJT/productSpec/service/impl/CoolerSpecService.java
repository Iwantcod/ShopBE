package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.CoolerSpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.CoolerSpec;
import com.example.shopPJT.productSpec.repository.CoolerSpecRepository;
import com.example.shopPJT.productSpec.service.ProductSpecServiceStrategy;
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
public class CoolerSpecService implements ProductSpecServiceStrategy<CoolerSpecDto> {
    private final CoolerSpecRepository coolerSpecRepository;
    @Autowired
    public CoolerSpecService(CoolerSpecRepository coolerSpecRepository) {
        this.coolerSpecRepository = coolerSpecRepository;
    }

    private CoolerSpecDto toDto(CoolerSpec coolerSpec) {
        CoolerSpecDto coolerSpecDto = new CoolerSpecDto();
        BeanUtils.copyProperties(coolerSpec, coolerSpecDto);
        return coolerSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.COOLER.toString().toLowerCase();
    }

    @Override
    public Class<? extends ModelNameDto> getDtoClass() {
        return CoolerSpecDto.class;
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public CoolerSpecDto getSpecById(Long coolerSpecId) {
        CoolerSpec coolerSpec = coolerSpecRepository.findById(coolerSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(coolerSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> coolerSpecs = coolerSpecRepository.searchByKeyword(pageable, modelName); //
        if(coolerSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return coolerSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> coolerSpecs = coolerSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(coolerSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Cooler Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return coolerSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(CoolerSpecDto specDto) {
        CoolerSpec coolerSpec = new CoolerSpec(); //
        BeanUtils.copyProperties(specDto, coolerSpec); //
        coolerSpecRepository.save(coolerSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(CoolerSpecDto specDto) {
        CoolerSpec coolerSpec = coolerSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getGroups() != null) coolerSpec.setGroups(specDto.getGroups());
        if(specDto.getNoise() != null) coolerSpec.setNoise(specDto.getNoise());
        if(specDto.getManufacturer() != null) coolerSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getModelName() != null) coolerSpec.setModelName(specDto.getModelName());
        if(specDto.getFanSpeed() != null) coolerSpec.setFanSpeed(specDto.getFanSpeed());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return coolerSpecRepository.existsById(specId);
    }
}
