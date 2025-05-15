package com.example.shopPJT.productSpec.repository;

import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.MemorySpec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemorySpecRepository extends JpaRepository<MemorySpec, Long> {
    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM MemorySpec t WHERE t.modelName LIKE %:keyword%") // 키워드를 이용하여 조회
    Slice<ModelNameDto> searchByKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM MemorySpec t")
    Slice<ModelNameDto> findAllByCreatedAtDesc(Pageable pageable);
}
