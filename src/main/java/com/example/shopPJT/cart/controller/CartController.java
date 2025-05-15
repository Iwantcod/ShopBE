package com.example.shopPJT.cart.controller;

import com.example.shopPJT.cart.dto.ReqCartDto;
import com.example.shopPJT.cart.service.CartService;
import com.example.shopPJT.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
