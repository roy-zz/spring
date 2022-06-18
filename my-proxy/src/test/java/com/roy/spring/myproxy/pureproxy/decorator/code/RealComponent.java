package com.roy.spring.myproxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component {

    @Override
    public String operation() {
        log.info("RealComponent operation 호출");
        return "Data";
    }
}
