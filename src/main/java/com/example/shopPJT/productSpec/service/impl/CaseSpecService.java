package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.CaseSpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.CaseSpec;
import com.example.shopPJT.productSpec.repository.CaseSpecRepository;
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
public class CaseSpecService implements ProductSpecServiceStrategy<CaseSpecDto> {
    private final CaseSpecRepository caseSpecRepository;
    @Autowired
    public CaseSpecService(CaseSpecRepository caseSpecRepository) {
        this.caseSpecRepository = caseSpecRepository;
    }

    private CaseSpecDto toDto(CaseSpec caseSpec) {
        CaseSpecDto caseSpecDto = new CaseSpecDto();
        BeanUtils.copyProperties(caseSpec, caseSpecDto);
        return caseSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.CASE.toString().toLowerCase();
    }

    @Override
    public Class<? extends ModelNameDto> getDtoClass() {
        return CaseSpecDto.class;
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public CaseSpecDto getSpecById(Long caseSpecId) {
        CaseSpec caseSpec = caseSpecRepository.findById(caseSpecId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(caseSpec);
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> caseSpecs = caseSpecRepository.searchByKeyword(pageable, modelName);
        if(caseSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        return caseSpecs.getContent();
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> caseSpecs = caseSpecRepository.findAllByCreatedAtDesc(pageable);
        if(caseSpecs.isEmpty()){
            log.error("GET Product Spec FAIL: Cannot find Case Spec Data.");
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return caseSpecs.getContent();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(CaseSpecDto specDto) {
        CaseSpec caseSpec = new CaseSpec();
        BeanUtils.copyProperties(specDto, caseSpec);
        caseSpecRepository.save(caseSpec);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(CaseSpecDto specDto) {
        CaseSpec caseSpec = caseSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getGroups() != null) caseSpec.setGroups(specDto.getGroups());
        if(specDto.getInnerSpace() != null) caseSpec.setInnerSpace(specDto.getInnerSpace());
        if (specDto.getManufacturer() != null) caseSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getModelName() != null) caseSpec.setModelName(specDto.getModelName());

    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return caseSpecRepository.existsById(specId);
    }
}
