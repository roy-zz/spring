package com.roy.spring.myproxy.jdkdynamic.code;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeInvocationHandler implements InvocationHandler {
    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Call TimeProxy invoke()");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        log.info("End TimeProxy invoke() spent time = {}", endTime - startTime);
        return result;
    }
}
