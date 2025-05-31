package com.example.shopPJT.order.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.order.dto.ReqOrderDto;
import com.example.shopPJT.order.dto.ReqOrderItemsDto;
import com.example.shopPJT.order.entity.Order;
import com.example.shopPJT.order.repository.OrderRepository;
import com.example.shopPJT.orderItem.entity.OrderItems;
import com.example.shopPJT.orderItem.repository.OrderItemsRepository;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.product.service.ProductService;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderItemsRepository orderItemsRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.productService = productService;
    }

    // 이 서버에서는 PG사에서 해당 결제에 대한 인증이 완료되었을때 이 인증을 카드사에 직접 요청하는 기능만 수행한다. ( 추후 구현 예정 )
    // 우리 서버에서 또한 결제 품목 및 수량 정보를 보관한다.

    @Transactional
    public String preRequestOrder(ReqOrderDto reqOrderDto) {
        // 1. 주문 유저, amount, 주문 주소, 주문 연락처 받아서 데이터베이스에 저장 -> 영속화 직전에 orderId 자동 생성(uuid)
        // 2. 주문 상품들의 재고량을 주문 수량만큼 감소
        // 3. orderItems 테이블에 주문 요소, 수량 정보 저장
        // 4. 클라이언트로 orderId 반환

        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }

        // 1. 주문 유저, amount, 주문 주소, 주문 연락처 받아서 데이터베이스에 저장 -> 영속화 직전에 orderId 자동 생성(uuid)
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));

        // 모든 주문 요소의 재고가 성공적으로 확보되어야 주문 성공 처리 -> 하나라도 실패하면 주문 전체 롤백
        // 주문 대상 상품의 기본키만 추출하여 별도의 리스트 생성
        List<Long> productIds = reqOrderDto.getOrderItems().stream().map(ReqOrderItemsDto::getProductId).collect(Collectors.toList());
        // productIds의 기본키들을 IN 절에 넣고 기본키 오름차순으로 비관적 락 획득 -> 순환 대기 상태 발생 방지
        List<Product> products = productRepository.findByIdsWithPessimisticLock(productIds);

        Order order = Order.builder()
                .user(user)
                .address(reqOrderDto.getOrderAddress())
                .phone(reqOrderDto.getPhone())
                .build();
        orderRepository.save(order); // Order의 @Prepersist 메소드 실행: pgOrderId 생성(uuid)

        int amount = 0;
        for(ReqOrderItemsDto item : reqOrderDto.getOrderItems()) {
            // 이 상품 조회 메소드 호출은 쿼리문을 실행하지 않는다: JPA 영속성 컨텍스트에 이미 로드된 상태이기 때문이다. (productIds)
            Product curProduct = productRepository.findById(item.getProductId()).orElseThrow(() ->
                    new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));

            // 2. 주문 상품의 재고량을 주문 수량만큼 감소
            if(curProduct.getInventory() - item.getQuantity() < 0) {
                // 재고를 감소시켰을 때 음수가 나온다면 주문 불가
                throw new ApplicationException(ApplicationError.PRODUCT_OUT_OF_STOCK);
            }
            curProduct.setInventory(curProduct.getInventory() - item.getQuantity());

            // 주문의 총 결제 금액 계산
            amount += curProduct.getPrice() * item.getQuantity();

            // 3. orderItems 테이블에 주문 요소, 수량 정보 저장.
            OrderItems orderItem = OrderItems.builder()
                    .order(order)
                    .product(curProduct)
                    .quantity(item.getQuantity())
                    .build();
            orderItemsRepository.save(orderItem);
        }
        order.setAmount(amount);
        // 4. 클라이언트로 orderId 반환
        return order.getPgOrderId().toString();
    }

    public void orderApproveRequest(ReqOrderDto reqOrderDto) {
        /*

            pg사로 결제 승인 요청하는 코드 작성 예정

         */
    }

}
