package com.example.shopPJT.cart.controller;

import com.example.shopPJT.cart.dto.ReqCartDto;
import com.example.shopPJT.cart.dto.ResCartDto;
import com.example.shopPJT.cart.service.CartService;
import com.example.shopPJT.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "장바구니 API", description = "요청에 담긴 jwt의 내부 권한이 ADMIN인 경우에만 응답")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @Operation(summary = "장바구니 상품 추가")
    public ResponseEntity<Void> addCart(@Valid @RequestBody ReqCartDto reqCartDto) {
        cartService.addCart(reqCartDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    @Operation(summary = "자신의 장바구니 리스트 조회")
    public ResponseEntity<List<ResCartDto>> getCart() {
        List<ResCartDto> resCartDtoList = cartService.getCartListByUserId();
        return ResponseEntity.ok(resCartDtoList);
    }

    @PatchMapping("/up/{cartId}") // 장바구니 수량 증가
    @Operation(summary = "장바구니 수량 1 증가")
    public ResponseEntity<Void> upCart(@PathVariable Long cartId) {
        cartService.updateCart(cartId, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/down/{cartId}") // 장바구니 수량 감소
    @Operation(summary = "장바구니 수량 1 감소")
    public ResponseEntity<Void> downCart(@PathVariable Long cartId) {
        cartService.updateCart(cartId, false);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}") // 장바구니 삭제
    @Operation(summary = "장바구니 요소 제거")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }
}
