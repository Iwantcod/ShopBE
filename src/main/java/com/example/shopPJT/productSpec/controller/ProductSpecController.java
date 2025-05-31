package com.example.shopPJT.productSpec.controller;

import com.example.shopPJT.productSpec.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spec")
@Tag(name = "모델 스펙 정보 API", description = "카테고리 이름은 반드시 소문자로 작성")
public class ProductSpecController {
    private final ProductSpecServiceFactory productSpecServiceFactory;
    @Autowired
    public ProductSpecController(ProductSpecServiceFactory productSpecServiceFactory) {
        this.productSpecServiceFactory = productSpecServiceFactory;
    }

    @GetMapping("/{categoryName}/id/{productSpecId}")
    @Operation(summary = "식별자로 조회", description = "상품 상세 정보 창에서 호출 가능")
    public ResponseEntity<?> findById(@PathVariable String categoryName, @PathVariable Long productSpecId) {
        return ResponseEntity.ok().body(productSpecServiceFactory.getStrategy(categoryName).getSpecById(productSpecId));
    }

    @GetMapping("/{categoryName}/latest/{startOffset}")
    @Operation(summary = "등록일자 기준 최신순 조회(페이징)")
    public ResponseEntity<?> findLatest(@PathVariable String categoryName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productSpecServiceFactory.getStrategy(categoryName).getSpecListPaging(startOffset));
    }

    @GetMapping("/{categoryName}/name/{modelName}/{startOffset}")
    @Operation(summary = "해당 키워드가 포함된 제품 정보 리스트 조회(페이징)")
    public ResponseEntity<?> searchByModelName(@PathVariable String categoryName, @PathVariable String modelName, @PathVariable Integer startOffset) {
        return ResponseEntity.ok().body(productSpecServiceFactory.getStrategy(categoryName).getSpecByModelName(modelName, startOffset));
    }
}
