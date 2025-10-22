package com.example.shopPJT.productSpec.repository;

import com.example.shopPJT.productSpec.dto.ModelNameDto;
import com.example.shopPJT.productSpec.entity.StorageSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageSpecRepository extends JpaRepository<StorageSpec, Long> {
    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM StorageSpec t WHERE t.modelName LIKE %:keyword%") // 키워드를 이용하여 조회
    Slice<ModelNameDto> searchByKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT new com.example.shopPJT.productSpec.dto.ModelNameDto(t.id, t.modelName, t.manufacturer)" + "FROM StorageSpec t")
    Slice<ModelNameDto> findAllByCreatedAtDesc(Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        UPDATE storage_spec AS s
        JOIN (
          SELECT
              spec_id,
              FLOOR(
                CASE
                  WHEN MAX(cnt) >= 30
                    THEN AVG(CASE WHEN pr BETWEEN 0.05 AND 0.95 THEN price END)
                  ELSE AVG(price)
                END
              ) AS final_avg
          FROM (
            SELECT
                p.logicalfk AS spec_id,
                p.price      AS price,
                COUNT(*) OVER (PARTITION BY p.logicalfk)                        AS cnt,
                PERCENT_RANK() OVER (PARTITION BY p.logicalfk ORDER BY p.price) AS pr
            FROM product p
            WHERE p.category_id = 8
          ) AS priced
          GROUP BY spec_id
        ) AS a
          ON s.storagespec_id = a.spec_id
        SET s.avg_price = a.final_avg;
    """, nativeQuery = true)
    int updateAvgPrice();
}
