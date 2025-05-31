package com.example.shopPJT.cart.service;

import com.example.shopPJT.cart.dto.ReqCartDto;
import com.example.shopPJT.cart.dto.ResCartDto;
import com.example.shopPJT.cart.entity.Cart;
import com.example.shopPJT.cart.repository.CartRepository;
import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }



    @Async // 장바구니에 상품 추가
    @Transactional
    public String addCart(ReqCartDto reqCartDto) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        Optional<Cart> existCart = cartRepository.findByUserIdAndProductId(userId, reqCartDto.getProductId());
        if(existCart.isPresent()) {
            // 이미 해당 유저의 장바구니에 해당 상품 정보가 존재하는 경우, 요소를 새로 추가하지 않고 '개수' 정보만 증가
            Integer prevQuantity = existCart.get().getQuantity();
            existCart.get().setQuantity(prevQuantity + reqCartDto.getQuantity()); // 트랜잭션 커밋 시점에서 더티체킹하여 변경사항 반영된다.
            return "장바구니에 상품이 추가되었습니다.";
        }

        User user = userRepository.findById(userId).orElseThrow(()
                -> new ApplicationException(ApplicationError.USER_NOT_FOUND));

        Product product = productRepository.findById(reqCartDto.getProductId()).orElseThrow(()
                -> new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(reqCartDto.getQuantity());
        cartRepository.save(cart);
        return "장바구니에 상품이 추가되었습니다.";
    }

    @Async
    @Transactional
    public void updateCart(Long cartId, Boolean isUp) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new ApplicationException(ApplicationError.CARTID_NOT_FOUND));
        Integer prevQuantity = cart.getQuantity();

        if(isUp) {
            cart.setQuantity(prevQuantity + 1);
        } else {
            if(prevQuantity - 1 < 1) {
                throw new ApplicationException(ApplicationError.CART_QUANTITY_INVALID);
            }
            cart.setQuantity(prevQuantity - 1);
        }
    }

    @Async
    @Transactional
    public void deleteCart(Long cartId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        if(cartId == null) {
            throw new ApplicationException(ApplicationError.CARTID_NOT_FOUND);
        }
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ApplicationException(ApplicationError.CARTID_NOT_FOUND));
        if(!cart.getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        cartRepository.deleteById(cartId);
    }


    @Transactional(readOnly = true) // 특정 유저의 장바구니에 담긴 상품 정보 및 수량 정보 등을 조회하고 반환
    public List<ResCartDto> getCartListByUserId() {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        List<ResCartDto> resCartDtoList = cartRepository.findCartAndProductByUserId(userId);
        if(resCartDtoList.isEmpty()) {
            throw new ApplicationException(ApplicationError.CART_NOT_FOUND);
        }

        return resCartDtoList;
    }


}
