package com.example.shopPJT.product.repository;

import com.example.shopPJT.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 조회 결과 행에 비관적 락의 쓰기 락을 활성화한다. -> 락이 활성화되어도 일반적인 select 문에는 조회된다.
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByIdWithPessimisticLock(@Param("productId") Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 상품 식별자 리스트를 인자로 받고, 식별자 오름차순 순서대로 비관적 락을 획득한다.
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds ORDER BY p.id ASC")
    List<Product> findByIdsWithPessimisticLock(List<Long> productIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :userId") // 상품 soft-delete
    void setAllProductDeleteTrueByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p JOIN FETCH p.user WHERE p.user.id = :userId AND p.isDeleted = false")
    Slice<Product> findAllActiveByUserId(Pageable pageable, @Param("userId") Long userId); // 특정 판매자의 게시 상품 페이징 조회


    // '삭제' 상태가 아닌 상품 정보 10개 페이징 조회
    @Query("SELECT p FROM Product p JOIN FETCH p.user WHERE p.isDeleted = false AND p.category.id = :categoryId")
    Slice<Product> findAllActiveProduct(Pageable pageable, @Param("categoryId") Integer categoryId);

    /**
     * 특정 카테고리의 상품 가격 높은 순 조회
     * @param categoryName
     * @param pageSize
     * @param offset
     * @return
     */
    @Query("""
        SELECT p.id
        FROM Product p
        WHERE p.category.id = (SELECT c.id FROM Category c WHERE c.name = :categoryName)
        AND p.isDeleted = false
        ORDER BY p.price DESC
        LIMIT :pageSize OFFSET :offset
    """)
    List<Long> findProductIdsByCategoryNameOrderByPriceDesc(@Param("categoryName") String categoryName, @Param("pageSize") int pageSize, @Param("offset") int offset);

    /**
     * 특정 카테고리의 상품 가격 낮은 순 조회
     * @param categoryName
     * @param pageSize
     * @param offset
     * @return
     */
    @Query("""
        SELECT p.id
        FROM Product p
        WHERE p.category.id = (SELECT c.id FROM Category c WHERE c.name = :categoryName)
        AND p.isDeleted = false
        ORDER BY p.price ASC
        LIMIT :pageSize OFFSET :offset
    """)
    List<Long> findProductIdsByCategoryNameOrderByPriceAsc(@Param("categoryName") String categoryName, @Param("pageSize") int pageSize, @Param("offset") int offset);

    /**
     * 특정 카테고리의 상품 최신순 조회
     * @param categoryName
     * @param pageSize
     * @param offset
     * @return
     */
    @Query("""
    SELECT p.id FROM Product p
    WHERE p.category.id = (SELECT c.id FROM Category c WHERE c.name = :categoryName) AND p.isDeleted = false
    ORDER BY p.createdAt DESC, p.id DESC
    LIMIT :pageSize OFFSET :offset
    """)
    List<Long> findActiveProductIdListByCategoryNameOrderByCreatedAtDesc(@Param("categoryName") String categoryName, @Param("pageSize") long pageSize, @Param("offset") long offset);

    /**
     * 카테고리 분류 상관 없이 최신순 조회(현재 관리자 기능)
     * @param pageSize
     * @param offset
     * @return
     */
    @Query("""
    SELECT p.id FROM Product p
    WHERE p.isDeleted = false
    ORDER BY p.createdAt DESC, p.id DESC
    LIMIT :pageSize OFFSET :offset
    """)
    List<Long> findAllActiveProductIdWithoutCategory(@Param("pageSize") long pageSize, @Param("offset") long offset);


    /**
     * 상품 식별자 리스트를 통해 해당 상품과 유저를 조인한 결과를 반환
     * @param productIds 상품 리스트
     * @return
     */
    @Query("""
    SELECT p FROM Product p
    JOIN FETCH p.user
    WHERE p.id IN :productIds
    """)
    List<Product> findProductListJoinUsers(List<Long> productIds);

    @Query("""
        SELECT p FROM Product p
        JOIN FETCH p.user
        WHERE (p.name LIKE CONCAT('%', :key, '%'))
        AND p.isDeleted = false
        """)
    Slice<Product> findAllByNameKey(Pageable pageable, @Param("key") String key);
}
