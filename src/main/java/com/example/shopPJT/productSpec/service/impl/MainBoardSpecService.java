package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.MainBoardSpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.MainBoardSpec;
import com.example.shopPJT.productSpec.repository.MainBoardSpecRepository;
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
public class MainBoardSpecService implements ProductSpecServiceStrategy<MainBoardSpecDto> {
    private final MainBoardSpecRepository mainBoardSpecRepository;
    @Autowired
    public MainBoardSpecService(MainBoardSpecRepository mainBoardSpecRepository) {
        this.mainBoardSpecRepository = mainBoardSpecRepository;
    }

    private MainBoardSpecDto toDto(MainBoardSpec mainBoardSpec) {
        MainBoardSpecDto mainBoardSpecDto = new MainBoardSpecDto();
        BeanUtils.copyProperties(mainBoardSpec, mainBoardSpecDto);
        return mainBoardSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.MAINBOARD.toString().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public MainBoardSpecDto getSpecById(Long mainBoardSpecId) { //
        MainBoardSpec mainBoardSpec = mainBoardSpecRepository.findById(mainBoardSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(mainBoardSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> mainBoardSpecs = mainBoardSpecRepository.searchByKeyword(pageable, modelName); //
        if(mainBoardSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return mainBoardSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> mainBoardSpecs = mainBoardSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(mainBoardSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find MainBoard Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return mainBoardSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(MainBoardSpecDto mainBoardSpecDto) { //
        // 관리자만 모델 스펙 정보 추가 가능
        if(!AuthUtil.getCurrentUserAuthority().equals("ROLE_ADMIN")) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }
        MainBoardSpec mainBoardSpec = new MainBoardSpec(); //
        BeanUtils.copyProperties(mainBoardSpecDto, mainBoardSpec); //
        mainBoardSpecRepository.save(mainBoardSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(MainBoardSpecDto specDto) {
        MainBoardSpec mainBoardSpec = mainBoardSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getChipSetType() != null) mainBoardSpec.setChipSetType(specDto.getChipSetType());
        if(specDto.getModelName() != null) mainBoardSpec.setModelName(specDto.getModelName());
        if(specDto.getCpuSocket() != null) mainBoardSpec.setCpuSocket(specDto.getCpuSocket());
        if(specDto.getMosFet() != null) mainBoardSpec.setMosFet(specDto.getMosFet());
        if(specDto.getManufacturer() != null) mainBoardSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getGroups() != null) mainBoardSpec.setGroups(specDto.getGroups());
        if(specDto.getModelGroups() != null) mainBoardSpec.setModelGroups(specDto.getModelGroups());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return mainBoardSpecRepository.existsById(specId);
    }
}
