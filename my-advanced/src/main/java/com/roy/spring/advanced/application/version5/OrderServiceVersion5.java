package com.roy.spring.advanced.application.version5;

import com.roy.spring.advanced.trace.callback.TraceTemplate;
import com.roy.spring.advanced.trace.logtrace.LogTrace;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceVersion5 {

    private final OrderRepositoryVersion5 orderRepository;
    private final TraceTemplate template;

    public OrderServiceVersion5(OrderRepositoryVersion5 orderRepository, LogTrace trace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(trace);
    }

    public void orderItem(String itemId) {
        template.execute("OrderService.request()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }

}
