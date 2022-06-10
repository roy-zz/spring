package com.roy.spring.myproxy.application.version1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceVersion1Impl implements OrderServiceVersion1 {

    private final OrderRepositoryVersion1 orderRepository;

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

}
