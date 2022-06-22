package com.roy.spring.myproxy.config.version1_proxy.concrete_proxy;

import com.roy.spring.myproxy.application.version2.OrderServiceVersion2;
import com.roy.spring.myproxy.trace.TraceStatus;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceVersion2 {
    private final OrderServiceVersion2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceVersion2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("Call OrderServiceConcreteProxy.orderItem()");
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }

}
