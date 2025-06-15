package com.example.shopPJT.cart.repository;

import com.example.shopPJT.cart.dto.ResCartDto;
import com.example.shopPJT.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
    Optional<Cart> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT new com.example.shopPJT.cart.dto.ResCartDto(p.id, p.name, p.price, p.inventory, p.user.id, p.productImageUrl, p.descriptionImageUrl, c.id, c.quantity)" + "FROM Cart c JOIN c.product p WHERE c.user.id = :userId")
    List<ResCartDto> findCartAndProductByUserId(Long userId);


    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.product.id IN :productIds")
    Optional<Cart> findAllByUserIdAndProductId(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.product.id IN :productIds")
    int deleteAllByUserIdAndProductIds(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
}
