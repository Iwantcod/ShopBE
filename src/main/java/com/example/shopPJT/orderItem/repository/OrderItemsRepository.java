package com.example.shopPJT.orderItem.repository;

import com.example.shopPJT.orderItem.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
