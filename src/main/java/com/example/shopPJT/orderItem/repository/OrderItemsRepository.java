package com.example.shopPJT.orderItem.repository;

import com.example.shopPJT.order.entity.Order;
import com.example.shopPJT.orderItem.entity.OrderItems;
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
}
