package com.example.shopPJT.recommendedProduct.repository;

import com.example.shopPJT.recommendedProduct.dto.RecommendedPick;
import com.example.shopPJT.recommendedProduct.entity.RecommendedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendedProductRepository extends JpaRepository<RecommendedProduct, Long> {

    /**
     * 상품 가격 수정 시 이를 반영할 추천 견적 레코드 조회(조건에 해당되는 레코드 중 가장 최근에 생성된 것)
     * @param productId
     * @return
     */
    @Query("""
    select p.recommendedProductId from RecommendedProduct p
    where p.cpuProduct.id = :productId
            or p.graphicProduct.id = :productId
            or p.caseProduct.id = :productId
            or p.memoryProduct.id = :productId
            or p.powerProduct.id = :productId
            or p.mainboardProduct.id = :productId
            or p.coolerProduct.id = :productId
            or p.storageProduct.id = :productId
    order by p.createdAt desc
    limit 1
    """)
    Long getUpdateTargetId(@Param("productId") Long productId);

    /**
     * 상품 가격 감소 시 '견적 상품' 테이블의 레코드에도 수정사항 반영
     *
     * @param targetId  수정할 견적의 식별자
     * @param minusPrice 가격 감소분
     */
    @Modifying
    @Query("""
        update RecommendedProduct p set p.totalPrice = p.totalPrice - :minusPrice
        where p.recommendedProductId = :targetId
    """)
    void minusPriceByProductId(@Param("targetId") Long targetId, @Param("minusPrice") Integer minusPrice);


    /**
     * 상품 가격 증가 시 '견적 상품' 테이블의 레코드에도 수정사항 반영
     *
     * @param targetId 수정할 견적의 식별자
     * @param plusPrice 가격 증가분
     */
    @Modifying
    @Query("""
        update RecommendedProduct p set p.totalPrice = p.totalPrice + :plusPrice
        where p.recommendedProductId = :targetId
    """)
    void plusPriceByProductId(@Param("targetId") Long targetId, @Param("plusPrice") Integer plusPrice);


    /**
     * '견적 상품'을 생성할 수 있는 '견적 원본 식별자' 조회
     * @return
     */
    @Query(value = """
    SELECT recommended_original_id FROM recommended_original o
    JOIN graphic_spec    gra ON o.graphicspec_id    = gra.graphicspec_id
    WHERE EXISTS(SELECT 1 FROM product WHERE category_id = 1 AND logicalfk = o.cpuspec_id)
        AND (gra.manufacturer IS NULL OR EXISTS(SELECT 1 FROM product WHERE category_id = 2 AND logicalfk = gra.graphicspec_id))
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 3 AND logicalfk = o.casespec_id)
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 4 AND logicalfk = o.memoryspec_id)
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 5 AND logicalfk = o.powerspec_id)
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 6 AND logicalfk = o.mainboardspec_id)
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 7 AND logicalfk = o.coolerspec_id)
        AND EXISTS(SELECT 1 FROM product WHERE category_id = 8 AND logicalfk = o.storagespec_id);
    """, nativeQuery = true)
    List<Long> findPossibleOriginal();

    /**
     * 모든 '견적 원본'에 대한 '실제 상품 조합'을 반환
     * @return
     */
    @Query(value = """
    SELECT
        o.recommended_original_id AS recommendedOriginalId,
        o.recommended_usage_id AS recommendedUsageId,
            
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 1
           AND p.logicalfk = cpu.cpuspec_id
           AND ABS(cpu.avg_price - p.price)
                 <= FLOOR(cpu.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS cpuSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 2
           AND p.logicalfk = gra.graphicspec_id
           AND ABS(gra.avg_price - p.price)
                 <= FLOOR(gra.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS graphicSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 3
           AND p.logicalfk = cas.casespec_id
           AND ABS(cas.avg_price - p.price)
                 <= FLOOR(cas.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS caseSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 4
           AND p.logicalfk = mem.memoryspec_id
           AND ABS(mem.avg_price - p.price)
                 <= FLOOR(mem.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS memorySpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 5
           AND p.logicalfk = po.powerspec_id
           AND ABS(po.avg_price - p.price)
                 <= FLOOR(po.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS powerSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 6
           AND p.logicalfk = ma.mainboardspec_id
           AND ABS(ma.avg_price - p.price)
                 <= FLOOR(ma.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS mainboardSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 7
           AND p.logicalfk = coo.coolerspec_id
           AND ABS(coo.avg_price - p.price)
                 <= FLOOR(coo.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS coolerSpecId,
             
        (SELECT p.product_id
         FROM product p
         WHERE p.is_deleted = 0 AND p.category_id = 8
           AND p.logicalfk = st.storagespec_id
           AND ABS(st.avg_price - p.price)
                 <= FLOOR(st.avg_price * 0.05)
         ORDER BY p.volume DESC, p.product_id DESC
         LIMIT 1) AS storageSpecId
        
    FROM recommended_original o
    JOIN cpu_spec        cpu ON o.cpuspec_id        = cpu.cpuspec_id
    JOIN graphic_spec    gra ON o.graphicspec_id    = gra.graphicspec_id
    JOIN case_spec       cas ON o.casespec_id       = cas.casespec_id
    JOIN memory_spec     mem ON o.memoryspec_id     = mem.memoryspec_id
    JOIN power_spec      po  ON o.powerspec_id      = po.powerspec_id
    JOIN main_board_spec ma  ON o.mainboardspec_id  = ma.mainboardspec_id
    JOIN cooler_spec     coo ON o.coolerspec_id     = coo.coolerspec_id
    JOIN storage_spec    st  ON o.storagespec_id    = st.storagespec_id
    WHERE o.recommended_original_id IN (:originalIds)
    """, nativeQuery = true)
    List<RecommendedPick> findBestProductPerComponent(List<Long> originalIds);


    /**
     * 8개 카테고리의 상품의 가격 총합 계산
     * @param cpuId
     * @param graphicId
     * @param caseId
     * @param memoryId
     * @param powerId
     * @param mainboardId
     * @param coolerId
     * @param storageId
     * @return
     */
    @Query("""
        SELECT SUM(p.price) FROM Product p
            WHERE p.id IN (:cpuId, :graphicId, :caseId, :memoryId, :powerId, :mainboardId, :coolerId, :storageId)
    """)
    Integer getRecommendedTotalPrice(@Param("cpuId") Long cpuId, @Param("graphicId") Long graphicId, @Param("caseId") Long caseId,
                                     @Param("memoryId") Long memoryId, @Param("powerId") Long powerId, @Param("mainboardId") Long mainboardId,
                                     @Param("coolerId") Long coolerId, @Param("storageId") Long storageId);

    /**
     * 추천 견적 추가(Bulk Insert)
     * @param usageId
     * @param originalId
     * @param cpuId
     * @param graphicId
     * @param caseId
     * @param memoryId
     * @param powerId
     * @param mainboardId
     * @param coolerId
     * @param storageId
     */
    @Modifying
    @Query(value = """
        INSERT INTO recommended_product(recommended_original_id, recommended_usage_id,
            cpu_product_id, graphic_product_id, case_product_id, memory_product_id, power_product_id,
            mainboard_product_id, cooler_product_id, storage_product_id, total_price)
        VALUES (:originalId, :usageId, :cpuId, :graphicId, :caseId, :memoryId, :powerId, :mainboardId, :coolerId, :storageId, :totalPrice);
    """, nativeQuery = true)
    void bulkInsert(@Param("originalId") Long originalId, @Param("usageId") Integer usageId,
                    @Param("cpuId") Long cpuId, @Param("graphicId") Long graphicId, @Param("caseId") Long caseId,
                    @Param("memoryId") Long memoryId, @Param("powerId") Long powerId, @Param("mainboardId") Long mainboardId,
                    @Param("coolerId") Long coolerId, @Param("storageId") Long storageId, @Param("totalPrice") Integer totalPrice);

    @Query("""
        SELECT r
        FROM RecommendedProduct r
        LEFT JOIN FETCH r.cpuProduct LEFT JOIN FETCH r.graphicProduct LEFT JOIN FETCH r.caseProduct LEFT JOIN FETCH r.memoryProduct
        LEFT JOIN FETCH r.powerProduct LEFT JOIN FETCH r.mainboardProduct LEFT JOIN FETCH r.coolerProduct LEFT JOIN FETCH r.storageProduct
        WHERE r.recommendedUsage.recommendedUsageId = :usageId AND r.totalPrice <= :budget
        ORDER BY r.createdAt DESC, r.totalPrice DESC, r.recommendedProductId DESC
        LIMIT 1
    """)
    Optional<RecommendedProduct> findByUsageAndBudget(@Param("usageId") Integer usageId, @Param("budget") Integer budget);

    /**
     * 가장 최근 견적 중 가장 저렴한 견적 조회(추천 견적 조회 실패 시 반환할 견적)
     * @return
     */
    @Query("""
        SELECT r
        FROM RecommendedProduct r
        LEFT JOIN FETCH r.cpuProduct LEFT JOIN FETCH r.graphicProduct LEFT JOIN FETCH r.caseProduct LEFT JOIN FETCH r.memoryProduct
        LEFT JOIN FETCH r.powerProduct LEFT JOIN FETCH r.mainboardProduct LEFT JOIN FETCH r.coolerProduct LEFT JOIN FETCH r.storageProduct
        WHERE r.recommendedUsage.recommendedUsageId = :usageId
        ORDER BY r.createdAt DESC, r.totalPrice ASC, r.recommendedProductId DESC
        LIMIT 1
    """)
    Optional<RecommendedProduct> findLatestCheap(@Param("usageId") Integer usageId);
}

