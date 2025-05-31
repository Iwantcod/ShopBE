package com.example.shopPJT.order.repository;

import com.example.shopPJT.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
