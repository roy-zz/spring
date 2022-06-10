package com.roy.spring.advanced.application.version5;

import com.roy.spring.advanced.trace.callback.TraceTemplate;
import com.roy.spring.advanced.trace.logtrace.LogTrace;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryVersion5 {
    private final TraceTemplate template;

    public OrderRepositoryVersion5(LogTrace trace) {
        this.template = new TraceTemplate(trace);
    }

    public void save(String itemId) {
        template.execute("OrderRepository.save()", () -> {
           if (itemId.equals("exception")) {
               throw new IllegalStateException("예외 발생");
           }
           sleep(1000);
           return null;
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
