package com.example.shopPJT.cart.controller;

import com.example.shopPJT.cart.dto.ReqCartDto;
import com.example.shopPJT.cart.dto.ResCartDto;
import com.example.shopPJT.cart.service.CartService;
import com.example.shopPJT.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    @Operation(summary = "장바구니 상품 추가")
    public CompletableFuture<String> addCart(@Valid @RequestBody ReqCartDto reqCartDto) {
        Long userId = AuthUtil.getSecurityContextUserId();
        return cartService.addCart(reqCartDto, userId).thenApply((result) -> result);
    }

    @GetMapping("/my")
    @Operation(summary = "자신의 장바구니 리스트 조회")
    public ResponseEntity<List<ResCartDto>> getCart() {
        List<ResCartDto> resCartDtoList = cartService.getCartListByUserId();
        return ResponseEntity.ok(resCartDtoList);
    }

    @PatchMapping("/up/{cartId}") // 장바구니 수량 증가
    public CompletableFuture<ResponseEntity<?>> upCart(@PathVariable Long cartId) {
        return cartService.updateCart(cartId, true).thenApply((result) -> ResponseEntity.ok().build());
    }

    @PatchMapping("/down/{cartId}") // 장바구니 수량 감소
    public CompletableFuture<ResponseEntity<?>> downCart(@PathVariable Long cartId) {
        return cartService.updateCart(cartId, false).thenApply((result) -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/{cartId}") // 장바구니 삭제
    public CompletableFuture<ResponseEntity<?>> deleteCart(@PathVariable Long cartId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        return cartService.deleteCart(cartId, userId).thenApply((result) -> ResponseEntity.ok().body("해당 장바구니 항목이 삭제되었습니다."));
    }



}
