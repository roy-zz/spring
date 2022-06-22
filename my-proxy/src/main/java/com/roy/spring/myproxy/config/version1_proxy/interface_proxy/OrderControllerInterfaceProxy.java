package com.roy.spring.myproxy.config.version1_proxy.interface_proxy;

import com.roy.spring.myproxy.application.version1.OrderControllerVersion1;
import com.roy.spring.myproxy.trace.TraceStatus;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerVersion1 {
    private final OrderControllerVersion1 target;
    private final LogTrace logTrace;

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("Call OrderController.request()");
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }

}
