package com.roy.spring.myproxy.application.version3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceVersion3 {

    private final OrderRepositoryVersion3 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

}
