package com.example.shopPJT.order.controller;

import com.example.shopPJT.order.dto.ReqOrderDto;
import com.example.shopPJT.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pre-request")
    public ResponseEntity<?> preRequest(@Valid @RequestBody ReqOrderDto reqOrderDto) {
        orderService.preRequestOrder(reqOrderDto);
        return ResponseEntity.ok().build();
    }
}