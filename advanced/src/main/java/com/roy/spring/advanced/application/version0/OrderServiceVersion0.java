package com.roy.spring.advanced.application.version0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceVersion0 {

    private final OrderRepositoryVersion0 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

}
