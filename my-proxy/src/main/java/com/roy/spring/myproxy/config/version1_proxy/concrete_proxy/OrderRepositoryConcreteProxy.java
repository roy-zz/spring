package com.roy.spring.myproxy.config.version1_proxy.concrete_proxy;

import com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2;
import com.roy.spring.myproxy.trace.TraceStatus;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryConcreteProxy extends OrderRepositoryVersion2 {
    private final OrderRepositoryVersion2 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("Call OrderRepositoryConcreteProxy.save()");
            target.save(itemId);
            logTrace.end(status);
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }

}
