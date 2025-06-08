package com.example.shopPJT.orderItems.controller;

import com.example.shopPJT.orderItems.dto.ResOrderItemsDto;
import com.example.shopPJT.orderItems.service.OrderItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order-item")
@Tag(name = "주문 요소 API")
public class OrderItemsController {
    private final OrderItemsService orderItemsService;
    @Autowired
    public OrderItemsController(OrderItemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    @GetMapping("/{orderId}") // order의 모든 주문 요소를 조회: 주문 대상 상품의 간략한 정보까지 조회(join)
    @Operation(summary = "특정 주문의 주문 요소 조회", description = "주문 대상 상품의 간략한 정보까지 조회합니다.")
    public ResponseEntity<List<ResOrderItemsDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemsService.getOrderItemsByOrderId(orderId));
    }
}
