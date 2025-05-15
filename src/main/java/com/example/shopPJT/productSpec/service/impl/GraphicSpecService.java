package com.example.shopPJT.productSpec.service.impl;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.CategoryName;
import com.example.shopPJT.productSpec.dto.GraphicSpecDto;
import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.GraphicSpec;
import com.example.shopPJT.productSpec.repository.GraphicSpecRepository;
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
public class GraphicSpecService implements ProductSpecServiceStrategy<GraphicSpecDto> {
    private final GraphicSpecRepository graphicSpecRepository;
    @Autowired
    public GraphicSpecService(GraphicSpecRepository graphicSpecRepository) {
        this.graphicSpecRepository = graphicSpecRepository;
    }

    private GraphicSpecDto toDto(GraphicSpec graphicSpec) {
        GraphicSpecDto graphicSpecDto = new GraphicSpecDto();
        BeanUtils.copyProperties(graphicSpec, graphicSpecDto);
        return graphicSpecDto;
    }

    @Override
    public String getSpecType() {
        return CategoryName.GRAPHIC.toString().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true) // 식별자로 조회
    public GraphicSpecDto getSpecById(Long graphicSpecId) { //
        GraphicSpec graphicSpec = graphicSpecRepository.findById(graphicSpecId) //
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        return toDto(graphicSpec); //
    }

    @Override
    @Transactional(readOnly = true) // '키워드'로 조회된 리스트를 응답(페이징)
    public List<ModelNameDto> getSpecByModelName(String modelName, Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> graphicSpecs = graphicSpecRepository.searchByKeyword(pageable, modelName); //
        if(graphicSpecs.isEmpty()) throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND); //
        return graphicSpecs.getContent(); //
    }

    @Override
    @Transactional(readOnly = true) // 등록 최신 순 10개 제품 정보 조회(페이징)
    public List<ModelNameDto> getSpecListPaging(Integer startOffset) {
        if(startOffset == null) startOffset = 0; // startOffset값이 누락된 경우 0으로 설정
        int pageSize = 10;

        Pageable pageable = PageRequest.of(startOffset, pageSize, Sort.by("createdAt").descending());
        Slice<ModelNameDto> graphicSpecs = graphicSpecRepository.findAllByCreatedAtDesc(pageable); //
        if(graphicSpecs.isEmpty()){ //
            log.error("GET Product Spec FAIL: Cannot find Graphic Spec Data."); //
            throw new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND);
        }
        return graphicSpecs.getContent(); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 추가 가능
    public void createSpec(GraphicSpecDto graphicSpecDto) { //
        GraphicSpec graphicSpec = new GraphicSpec(); //
        BeanUtils.copyProperties(graphicSpecDto, graphicSpec); //
        graphicSpecRepository.save(graphicSpec); //
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 모델 스펙 정보 수정 가능
    public void updateSpec(GraphicSpecDto specDto) {
        GraphicSpec graphicSpec = graphicSpecRepository.findById(specDto.getId())
                .orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        if(specDto.getBoostClock() != null) graphicSpec.setBoostClock(specDto.getBoostClock());
        if(specDto.getVram() != null) graphicSpec.setVram(specDto.getVram());
        if(specDto.getSeries() != null) graphicSpec.setSeries(specDto.getSeries());
        if(specDto.getCoreClock() != null) graphicSpec.setCoreClock(specDto.getCoreClock());
        if(specDto.getChipSetType() != null) graphicSpec.setChipSetType(specDto.getChipSetType());
        if(specDto.getRecommendPower() != null) graphicSpec.setRecommendPower(specDto.getRecommendPower());
        if(specDto.getManufacturer() != null) graphicSpec.setManufacturer(specDto.getManufacturer());
        if(specDto.getModelName() != null) graphicSpec.setModelName(specDto.getModelName());
        if(specDto.getChipSetManufacturer() != null) graphicSpec.setChipSetManufacturer(specDto.getChipSetManufacturer());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isExist(Long specId) {
        return graphicSpecRepository.existsById(specId);
    }
}
