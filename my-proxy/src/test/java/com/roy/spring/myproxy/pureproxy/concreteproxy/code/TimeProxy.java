package com.roy.spring.myproxy.pureproxy.concreteproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeProxy extends ConcreteLogic {
    private final ConcreteLogic concreteLogic;

    @Override
    public String operation() {
        log.info("Call TimeProxy.operation()");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation();

        long endTime = System.currentTimeMillis();
        log.info("ResultTime = {}", endTime - startTime);
        return result;
    }

}
