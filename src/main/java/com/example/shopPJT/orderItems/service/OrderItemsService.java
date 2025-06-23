package com.example.shopPJT.orderItems.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.orderItems.dto.ResOrderItemsDto;
import com.example.shopPJT.orderItems.entity.OrderItems;
import com.example.shopPJT.orderItems.repository.OrderItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderItemsService {
    private final OrderItemsRepository orderItemsRepository;
    @Autowired
    public OrderItemsService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    private ResOrderItemsDto toDto(OrderItems orderItems) {
        ResOrderItemsDto resOrderItemsDto = new ResOrderItemsDto();
        resOrderItemsDto.setProductId(orderItems.getProduct().getId());
        resOrderItemsDto.setProductName(orderItems.getProduct().getName());
        resOrderItemsDto.setQuantity(orderItems.getQuantity());
        resOrderItemsDto.setProductImageUrl(orderItems.getProduct().getProductImageUrl());
        resOrderItemsDto.setSellerUserId(orderItems.getProduct().getUser().getId());
        resOrderItemsDto.setCategoryId(orderItems.getCategory().getId());
        return resOrderItemsDto;
    }

    @Transactional(readOnly = true)
    public List<ResOrderItemsDto> getOrderItemsByOrderId(Long orderId) {
        if(orderId == null) {
            throw new ApplicationException(ApplicationError.ORDER_NOT_FOUND);
        }

        List<OrderItems> orderItems = orderItemsRepository.findOrderProductByOrderId(orderId);
        if(orderItems.isEmpty()) {
            throw new ApplicationException(ApplicationError.WRONG_REQUEST);
        }
        return orderItems.stream().map(this::toDto).toList();
    }

}
