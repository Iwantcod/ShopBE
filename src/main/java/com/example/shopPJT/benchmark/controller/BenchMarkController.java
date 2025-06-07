package com.example.shopPJT.benchmark.controller;

import com.example.shopPJT.benchmark.service.BenchMarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benchmark")
@Tag(name = "벤치마크 API")
public class BenchMarkController {
    private final BenchMarkService benchMarkService;
    @Autowired
    public BenchMarkController(BenchMarkService benchMarkService) {
        this.benchMarkService = benchMarkService;
    }

    @GetMapping
    @Operation(summary = "cpu, graphic 식별자를 통해 벤치마크 값 조회")
    public ResponseEntity<?> getBenchMark(@RequestParam Long cpuSpecId, @RequestParam Long graphicSpecId) {
        if(cpuSpecId == null || graphicSpecId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(benchMarkService.getBenchMark(cpuSpecId, graphicSpecId));
    }
}
