package com.roy.spring.basic.service;

import com.roy.spring.basic.domain.Order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
