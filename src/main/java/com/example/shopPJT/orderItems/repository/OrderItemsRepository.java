package com.example.shopPJT.orderItems.repository;

import com.example.shopPJT.orderItems.dto.ResOrderItemsDto;
import com.example.shopPJT.orderItems.entity.OrderItems;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM OrderItems o WHERE o.order.id = :orderId")
    void deleteByOrderIdBulk(@Param("orderId") Long orderId);

    @Query("SELECT o FROM OrderItems o WHERE o.order.id = :orderId")
    List<OrderItems> findAllByOrderId(@Param("orderId") Long orderId);

    @Query("""
            SELECT new com.example.shopPJT.orderItems.dto.ResOrderItemsDto
                    (p.id, p.name, p.user.id, p.productImageUrl, o.quantity)
            FROM OrderItems o JOIN o.product p
            WHERE o.order.id = :orderId
        """)
    List<ResOrderItemsDto> findOrderProductByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT count(o) FROM OrderItems o WHERE o.product.id = :productId AND o.user.id = :userId")
    int checkProductPurchase(@Param("productId") Long productId, @Param("userId") Long userId);
}