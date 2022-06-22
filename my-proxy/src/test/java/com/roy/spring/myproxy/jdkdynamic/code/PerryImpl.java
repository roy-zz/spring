package com.roy.spring.myproxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PerryImpl implements PerryInterface {
    @Override
    public String call() {
        log.info("Call Perry");
        return "PERRY";
    }
}
