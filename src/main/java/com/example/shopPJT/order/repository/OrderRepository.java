package com.example.shopPJT.order.repository;

import com.example.shopPJT.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPgOrderId(UUID pgOrderId);
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Orders o SET o.isDeleted = true WHERE o.id = :orderId")
    void deleteOrderBulk(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Orders o WHERE o.user.id = :userId AND o.isDeleted = false")
    List<Order> findByUserIdActive(@Param("userId") Long userId);
}
