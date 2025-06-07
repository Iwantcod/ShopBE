package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.CpuSpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.CpuSpec;
import com.example.shopPJT.productSpec.repository.CpuSpecRepository;
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
public class CpuSpecService implements ProductSpecServiceStrategy<CpuSpecDto> {
    private final CpuSpecRepository cpuSpecRepository;
    @Autowired
    public CpuSpecService(CpuSpecRepository cpuSpecRepository) {
        this.cpuSpecRepository = cpuSpecRepository;
    }

    private CpuSpecDto toDto(CpuSpec cpuSpec) {
        CpuSpecDto cpuSpecDto = new CpuSpecDto();
        BeanUtils.copyProperties(cpuSpec, cpuSpecDto);
        return cpuSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.CPU.toString().toLowerCase();
    }

    @Override
    public Class<? extends ModelNameDto> getDtoClass() {
        return CpuSpecDto.class;
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public CpuSpecDto getSpecById(Long cpuSpecId) { //
        CpuSpec cpuSpec = cpuSpecRepository.findById(cpuSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(cpuSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> cpuSpecs = cpuSpecRepository.searchByKeyword(pageable, modelName); //
        if(cpuSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return cpuSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> cpuSpecs = cpuSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(cpuSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Cpu Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return cpuSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(CpuSpecDto specDto) { //
        CpuSpec cpuSpec = new CpuSpec(); //
        BeanUtils.copyProperties(specDto, cpuSpec); //
        cpuSpecRepository.save(cpuSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(CpuSpecDto specDto) {
        CpuSpec cpuSpec = cpuSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getBoostClock() != null) cpuSpec.setBoostClock(specDto.getBoostClock());
        if(specDto.getCoreNum() != null) cpuSpec.setCoreNum(specDto.getCoreNum());
        if(specDto.getManufacturer() != null) cpuSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getModelName() != null) cpuSpec.setModelName(specDto.getModelName());
        if(specDto.getThreadNum() != null) cpuSpec.setThreadNum(specDto.getThreadNum());
        if(specDto.getL3Cache() != null) cpuSpec.setL3Cache(specDto.getL3Cache());
        if(specDto.getProcessSize() != null) cpuSpec.setProcessSize(specDto.getProcessSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return cpuSpecRepository.existsById(specId);
    }
}
