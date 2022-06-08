package com.roy.spring.advanced.application.version2;

import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.mytrace.MyTraceVersion2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerVersion2 {

    private final OrderServiceVersion2 orderService;
    private final MyTraceVersion2 trace;

    @GetMapping("/v2/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(), itemId);
            trace.end(status);
            return "OK";
        } catch (Exception exception) {
            trace.exception(status, exception);
            throw exception;
        }
    }

}
