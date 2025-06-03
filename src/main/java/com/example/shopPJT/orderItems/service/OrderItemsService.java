package com.example.shopPJT.orderItems.service;

import com.example.shopPJT.global.exception.ApplicationError;
import com.example.shopPJT.global.exception.ApplicationException;
import com.example.shopPJT.orderItems.dto.ResOrderItemsDto;
import com.example.shopPJT.orderItems.repository.OrderItemsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class OrderItemsService {
    private final OrderItemsRepository orderItemsRepository;
    @Autowired
    public OrderItemsService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    @Transactional(readOnly = true)
    public List<ResOrderItemsDto> getOrderItemsByOrderId(Long orderId) {
        if(orderId == null) {
            throw new ApplicationException(ApplicationError.ORDER_NOT_FOUND);
        }

        List<ResOrderItemsDto> resOrderItemsDtos = orderItemsRepository.findOrderProductByOrderId(orderId);
        if(resOrderItemsDtos.isEmpty()) {
            throw new ApplicationException(ApplicationError.WRONG_REQUEST);
        }
        return resOrderItemsDtos;
    }

}
