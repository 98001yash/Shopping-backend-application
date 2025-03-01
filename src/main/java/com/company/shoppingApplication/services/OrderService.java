package com.company.shoppingApplication.services;

import com.company.shoppingApplication.dto.OrderDto;
import com.company.shoppingApplication.entities.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
