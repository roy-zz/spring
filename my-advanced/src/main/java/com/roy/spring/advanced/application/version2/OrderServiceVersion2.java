package com.roy.spring.advanced.application.version2;

import com.roy.spring.advanced.trace.TraceId;
import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.mytrace.MyTraceVersion2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceVersion2 {

    private final OrderRepositoryVersion2 orderRepository;
    private final MyTraceVersion2 trace;

    public void orderItem(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception exception) {
            trace.exception(status, exception);
            throw exception;
        }
    }

}
