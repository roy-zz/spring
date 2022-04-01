package com.roy.spring.service;

import com.roy.spring.domain.Order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
