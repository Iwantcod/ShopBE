package com.example.shopPJT.order.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.order.dto.*;
import com.example.shopPJT.order.entity.DeliveryStatus;
import com.example.shopPJT.order.entity.Order;
import com.example.shopPJT.order.repository.OrderRepository;
import com.example.shopPJT.orderItems.entity.OrderItems;
import com.example.shopPJT.orderItems.repository.OrderItemsRepository;
import com.example.shopPJT.product.entity.Product;
import com.example.shopPJT.product.repository.ProductRepository;
import com.example.shopPJT.user.entity.User;
import com.example.shopPJT.user.repository.UserRepository;
import com.example.shopPJT.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderItemsRepository orderItemsRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    // 이 서버에서는 PG사에서 해당 결제에 대한 인증이 완료되었을때 이 인증을 카드사에 직접 요청하는 기능만 수행한다. ( 추후 구현 예정 )
    // 우리 서버에서 또한 결제 품목 및 수량 정보를 보관한다.

    private ResOrderDto toDto(Order order) {
        return ResOrderDto.builder()
                .orderId(order.getId())
                .amount(order.getAmount())
                .requestedAt(order.getRequested_at())
                .address(order.getAddress())
                .phone(order.getPhone())
                .deliveryStatus(order.getDeliveryStatus())
                .build();
    }

    @Transactional
    public ResOrderAuthDto preRequestOrder(ReqOrderDto reqOrderDto) {
        // 1. 주문 유저, amount, 주문 주소, 주문 연락처 받아서 데이터베이스에 저장 -> 영속화 직전에 orderId 자동 생성(uuid)
        // 2. 주문 상품들의 재고량을 주문 수량만큼 감소, amount(총 결제 금액) 계산
        // 3. orderItems 테이블에 주문 요소, 수량 정보 저장
        // 4. 클라이언트로 orderId, amount 반환

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
            Product curProduct = productRepository.findById(item.getProductId()).orElseThrow(()
                    -> new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));

            if(curProduct.getIsDeleted()) {
                // '삭제처리'된 상품이 포함되어 있다면 예외를 발생시킨다.
                orderRepository.deleteById(order.getId());
                throw new ApplicationException(ApplicationError.WRONG_REQUEST);
            }

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
        return ResOrderAuthDto.builder().orderId(order.getPgOrderId()).amount(amount).build();
    }

    @Transactional
    public boolean orderApproveRequest(ReqApproveOrderDto reqApproveOrderDto) {
        // 클라이언트로 받은 orderId(PG사로 전송했던 UUID 값)를 통해 주문 정보 조회
        Order order = orderRepository.findByPgOrderId(reqApproveOrderDto.getOrderId()).orElseThrow(() ->
                new ApplicationException(ApplicationError.ORDER_NOT_FOUND));
        // 삭제 처리된 요청에 대한 주문 승인 요청은 기각
        if(order.getIsDeleted()) {
            throw new ApplicationException(ApplicationError.ORDER_REQUEST_INVALID);
        }
        // 총 결제 금액 정보가 일치하지 않으면 안전하지 않은 결제 요청이라고 판단, 해당 주문 요청 삭제(soft-delete) 처리
        if(!order.getAmount().equals(reqApproveOrderDto.getAmount())) {
            approveOrderErrorHandler(order.getId()); // 별도의 트랜잭션으로 예외 처리로직 수행
            return false;
        }
        // 주문 정보에 paymentKey 값 저장
        order.setPgPaymentKey(reqApproveOrderDto.getPaymentKey());

        /*
            pg사로 결제 승인 요청하는 코드
            try {

            } catch() {

            }
         */

        return true;
    }

    // 주문 승인 예외 처리 로직
    private void approveOrderErrorHandler(Long orderId) {
        List<OrderItems> orderItems = orderItemsRepository.findAllByOrderId(orderId);
        List<Long> productIdsForRollback = orderItems.stream().map(item -> item.getProduct().getId()).toList();
        List<Product> products = productRepository.findByIdsWithPessimisticLock(productIdsForRollback);

        for(OrderItems item : orderItems) {
            Product curProduct = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ApplicationException(ApplicationError.PRODUCT_NOT_FOUND));
            // 주문 수량 만큼 재고량 복구
            curProduct.setInventory(curProduct.getInventory() + item.getQuantity());
        }
        orderItemsRepository.deleteByOrderIdBulk(orderId); // 주문 요소 삭제
        orderRepository.deleteOrderBulk(orderId); // 주문 정보 삭제
    }


    @Transactional(readOnly = true) // 자신의 주문 정보 조회(삭제처리된 것은 제외, '취소'처리된 것은 표시)
    public List<ResOrderDto> getMyOrderInfo() {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        List<Order> orders = orderRepository.findByUserIdActive(userId);
        if(orders.isEmpty()) {
            throw new ApplicationException(ApplicationError.ORDER_NOT_FOUND);
        }
        return orders.stream().map(this::toDto).toList();
    }


    // 주문에 대한 소유권 검증 후 조회된 주문 엔티티 리턴
    private Order vertificationAccessRights(Long orderId) {
        Long userId = AuthUtil.getSecurityContextUserId();
        if(userId == null) {
            throw new ApplicationException(ApplicationError.USERID_NOT_FOUND);
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ApplicationException(ApplicationError.ORDER_NOT_FOUND));
        // 자신의 주문 정보만 삭제/취소 가능
        if(!order.getUser().getId().equals(userId)) {
            throw new ApplicationException(ApplicationError.ACCESS_NOT_ALLOWED);
        }

        return order;
    }

    @Transactional // 주문 취소 메소드
    public void cancelOrderById(Long orderId) {
        Order order = vertificationAccessRights(orderId);
        if(order.getDeliveryStatus().equals(DeliveryStatus.PROCESSING)) {
            order.setDeliveryStatus(DeliveryStatus.CANCELED);
        } else {
            throw new ApplicationException(ApplicationError.ORDER_CANNOT_CANCEL);
        }
    }

    @Transactional
    public void softDeleteOrderById(Long orderId) {
        Order order = vertificationAccessRights(orderId);
        if(order.getDeliveryStatus().equals(DeliveryStatus.DELIVERED) || order.getDeliveryStatus().equals(DeliveryStatus.CANCELED)) {
            // 배송 완료되었거나 취소된 주문만 삭제 가능
            order.setDeleted(true);
        } else {
            throw new ApplicationException(ApplicationError.ORDER_CANNOT_DELETE);
        }
    }
}
