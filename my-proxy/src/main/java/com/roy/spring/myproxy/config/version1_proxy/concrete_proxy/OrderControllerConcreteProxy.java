package com.roy.spring.myproxy.config.version1_proxy.concrete_proxy;

import com.roy.spring.myproxy.application.version2.OrderControllerVersion2;
import com.roy.spring.myproxy.trace.TraceStatus;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerVersion2 {
    private final OrderControllerVersion2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerVersion2 target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("Call OrderControllerConcreteProxy.request()");
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }

}
