package com.example.shopPJT.productSpec.repository;

import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.GraphicSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GraphicSpecRepository extends JpaRepository<GraphicSpec, Long> {
    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM GraphicSpec t WHERE t.modelName LIKE %:keyword%") // 키워드를 이용하여 조회
    Slice<ModelNameDto> searchByKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM GraphicSpec t")
    Slice<ModelNameDto> findAllByCreatedAtDesc(Pageable pageable);
}
