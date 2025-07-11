package com.example.shopPJT.order.controller;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.order.dto.ReqApproveOrderDto;
import com.example.shopPJT.order.dto.ReqOrderDto;
import com.example.shopPJT.order.dto.ResOrderAuthDto;
import com.example.shopPJT.order.dto.ResOrderDto;
import com.example.shopPJT.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Tag(name = "주문 API")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/pre-request") // 클라이언트에서 PG사로 결제 요청 전 호출하는 API. orderId, 총 금액 정보 발급
    @Operation(summary = "PG사에게 결제 요청 전 호출해야하는 API", description = "orderId, amount 값을 발급합니다.")
    public ResponseEntity<ResOrderAuthDto> preRequest(@Valid @RequestBody ReqOrderDto reqOrderDto) {
        return ResponseEntity.ok().body(orderService.preRequestOrder(reqOrderDto));
    }

    @PostMapping("/approve-request") // 결제 승인 요청
    @Operation(summary = "결제 승인 요청", description = "실제 결제 승인 요청")
    public ResponseEntity<String> approveRequest(@Valid @RequestBody ReqApproveOrderDto reqApproveOrderDto) {
        if(orderService.orderApproveRequest(reqApproveOrderDto)) {
            return ResponseEntity.ok().body("결제가 완료되었습니다.");
        } else {
            throw new ApplicationException(ApplicationError.ORDER_REQUEST_INVALID);
        }
    }

    @GetMapping("/my") // 자신의 주문 정보 조회(삭제 처리된 주문은 조회하지 않음)
    @Operation(summary = "자신의 주문 내역 조회", description = "삭제 처리된 주문은 조회하지 않습니다.")
    public ResponseEntity<List<ResOrderDto>> getMyOrders() {
        return ResponseEntity.ok().body(orderService.getMyOrderInfo());
    }

    @PatchMapping("/cancel/{orderId}") // 사용자의 주문 취소 요청: 주문 상태가 '준비 중'인 주문만 취소 요청 가능
    @Operation(summary = "주문 취소", description = "'준비 중' 상태인 주문만 취소할 수 있습니다.")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrderById(orderId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}") // 취소 혹은 완료 상태의 주문만 삭제 가능
    @Operation(summary = "주문 내역 삭제", description = "'취소' 혹은 '완료' 상태인 주문만 삭제할 수 있습니다.")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.softDeleteOrderById(orderId);
        return ResponseEntity.ok().build();
    }
}