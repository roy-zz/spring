package com.roy.spring.myproxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoyImpl implements RoyInterface {
    @Override
    public String call() {
        log.info("Call Roy");
        return "ROY";
    }
}
