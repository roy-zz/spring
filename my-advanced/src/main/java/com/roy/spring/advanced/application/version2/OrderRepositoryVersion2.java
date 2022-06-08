package com.roy.spring.advanced.application.version2;

import com.roy.spring.advanced.trace.TraceId;
import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.mytrace.MyTraceVersion2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryVersion2 {

    private final MyTraceVersion2 trace;

    public void save(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderRepository.save()");
            if (itemId.equals("exception")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);
            trace.end(status);
        } catch (Exception exception) {
            trace.exception(status, exception);
            throw exception;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
