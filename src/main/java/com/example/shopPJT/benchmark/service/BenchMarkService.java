package com.example.shopPJT.benchmark.service;

import com.example.shopPJT.benchmark.dto.BenchMarkDto;
import com.example.shopPJT.benchmark.dto.ReqBenchMarkDto;
import com.example.shopPJT.benchmark.dto.ResBenchMarkDto;
import com.example.shopPJT.benchmark.entity.BenchMark;
import com.example.shopPJT.benchmark.repository.BenchMarkRepository;
import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.productSpec.entity.CpuSpec;
import com.example.shopPJT.productSpec.entity.GraphicSpec;
import com.example.shopPJT.productSpec.repository.CpuSpecRepository;
import com.example.shopPJT.productSpec.repository.GraphicSpecRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BenchMarkService {
    private final BenchMarkRepository benchMarkRepository;
    private final CpuSpecRepository cpuSpecRepository;
    private final GraphicSpecRepository graphicSpecRepository;

    @Autowired
    public BenchMarkService(BenchMarkRepository benchMarkRepository, CpuSpecRepository cpuSpecRepository, GraphicSpecRepository graphicSpecRepository) {
        this.benchMarkRepository = benchMarkRepository;
        this.cpuSpecRepository = cpuSpecRepository;
        this.graphicSpecRepository = graphicSpecRepository;
    }

    private ResBenchMarkDto toDto(BenchMark benchMark) {
        ResBenchMarkDto resBenchMarkDto = new ResBenchMarkDto();
        resBenchMarkDto.setAvgFrame1(benchMark.getAvgFrame1());
        resBenchMarkDto.setAvgFrame2(benchMark.getAvgFrame2());
        resBenchMarkDto.setAvgFrame3(benchMark.getAvgFrame3());
        resBenchMarkDto.setCpuSpecId(benchMark.getCpuSpec().getId());
        resBenchMarkDto.setGraphicSpecId(benchMark.getGraphicSpec().getId());
        resBenchMarkDto.setCpuModelName(benchMark.getCpuSpec().getModelName());
        resBenchMarkDto.setGraphicModelName(benchMark.getGraphicSpec().getModelName());
        return resBenchMarkDto;
    }

    @Transactional
    public void addBenchMark(ReqBenchMarkDto reqBenchMarkDto) {
        CpuSpec cpuSpec = cpuSpecRepository.findById(reqBenchMarkDto.getCpuSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));
        GraphicSpec graphicSpec = graphicSpecRepository.findById(reqBenchMarkDto.getGraphicSpecId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.PRODUCTSPEC_NOT_FOUND));

        BenchMark benchMark = new BenchMark();
        benchMark.setCpuSpec(cpuSpec);
        benchMark.setGraphicSpec(graphicSpec);
        benchMark.setAvgFrame1(reqBenchMarkDto.getAvgFrame1());
        benchMark.setAvgFrame2(reqBenchMarkDto.getAvgFrame2());
        benchMark.setAvgFrame3(reqBenchMarkDto.getAvgFrame3());
        benchMarkRepository.save(benchMark);
    }

    @Transactional // 성능 수치 값만 수정 가능
    public void updateBenchMark(Long benchMarkId, BenchMarkDto benchMarkDto) {
        BenchMark benchMark = benchMarkRepository.findById(benchMarkId).orElseThrow(() ->
                new ApplicationException(ApplicationError.BENCHMARK_NOT_FOUND));
        if(benchMarkDto.getAvgFrame1() != null){
            benchMark.setAvgFrame1(benchMark.getAvgFrame1());
        }
        if(benchMarkDto.getAvgFrame2() != null){
            benchMark.setAvgFrame2(benchMark.getAvgFrame2());
        }
        if(benchMarkDto.getAvgFrame3() != null){
            benchMark.setAvgFrame3(benchMark.getAvgFrame3());
        }
    }

    @Transactional
    public void deleteBenchMark(Long benchMarkId) {
        benchMarkRepository.deleteById(benchMarkId);
    }

    @Transactional(readOnly = true)
    public ResBenchMarkDto getBenchMark(Long cpuSpecId, Long graphicSpecId) {
        BenchMark benchMark = benchMarkRepository.findByCpuSpecIdAndGraphicSpecId(cpuSpecId, graphicSpecId).orElseThrow(() ->
                new ApplicationException(ApplicationError.BENCHMARK_NOT_FOUND));
        return toDto(benchMark);
    }
}
