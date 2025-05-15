package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.dto.PowerSpecDto;
import com.example.shopPJT.productSpec.entity.PowerSpec;
import com.example.shopPJT.productSpec.repository.PowerSpecRepository;
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
public class PowerSpecService implements ProductSpecServiceStrategy<PowerSpecDto> {
    private final PowerSpecRepository powerSpecRepository;
    @Autowired
    public PowerSpecService(PowerSpecRepository powerSpecRepository) {
        this.powerSpecRepository = powerSpecRepository;
    }

    private PowerSpecDto toDto(PowerSpec powerSpec) {
        PowerSpecDto powerSpecDto = new PowerSpecDto();
        BeanUtils.copyProperties(powerSpec, powerSpecDto);
        return powerSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.POWER.toString().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public PowerSpecDto getSpecById(Long powerSpecId) { //
        PowerSpec powerSpec = powerSpecRepository.findById(powerSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(powerSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> powerSpecs = powerSpecRepository.searchByKeyword(pageable, modelName); //
        if(powerSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return powerSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> powerSpecs = powerSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(powerSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Power Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return powerSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void createSpec(PowerSpecDto powerSpecDto) { //
        PowerSpec powerSpec = new PowerSpec(); //
        BeanUtils.copyProperties(powerSpecDto, powerSpec); //
        powerSpecRepository.save(powerSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(PowerSpecDto specDto) {
        PowerSpec powerSpec = powerSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getGroups() != null) powerSpec.setGroups(specDto.getGroups());
        if(specDto.getModelName() != null) powerSpec.setModelName(specDto.getModelName());
        if(specDto.getRatedOutputPower() != null) powerSpec.setRatedOutputPower(specDto.getRatedOutputPower());
        if(specDto.getManufacturer() != null) powerSpec.setManufacturer(specDto.getManufacturer());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return powerSpecRepository.existsById(specId);
    }
}
