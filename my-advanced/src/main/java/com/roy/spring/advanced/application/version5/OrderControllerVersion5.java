package com.roy.spring.advanced.application.version5;

import com.roy.spring.advanced.trace.callback.TraceTemplate;
import com.roy.spring.advanced.trace.logtrace.LogTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerVersion5 {

    private final OrderServiceVersion5 orderService;
    private final TraceTemplate template;

    public OrderControllerVersion5(OrderServiceVersion5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderController.request()", () -> {
            orderService.orderItem(itemId);
            return "OK";
        });
    }

}
