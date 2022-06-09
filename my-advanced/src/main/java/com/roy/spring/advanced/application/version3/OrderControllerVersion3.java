package com.roy.spring.advanced.application.version3;

import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerVersion3 {

    private final OrderServiceVersion3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "OK";
        } catch (Exception exception) {
            trace.exception(status, exception);
            throw exception;
        }
    }

}
