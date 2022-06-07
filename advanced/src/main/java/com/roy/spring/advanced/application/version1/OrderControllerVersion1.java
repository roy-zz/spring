package com.roy.spring.advanced.application.version1;

import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.mytrace.TraceVersion1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerVersion1 {

    private final OrderServiceVersion1 orderService;
    private final TraceVersion1 trace;

    @GetMapping("/v1/request")
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
