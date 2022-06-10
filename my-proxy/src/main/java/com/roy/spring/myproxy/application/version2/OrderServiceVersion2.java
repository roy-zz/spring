package com.roy.spring.myproxy.application.version2;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceVersion2 {

    private final OrderRepositoryVersion2 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

}
