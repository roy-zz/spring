package com.roy.spring.myproxy.pureproxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimeDecorator implements Component {
    private final Component component;

    @Override
    public String operation() {
        log.info("TimeDecorator operation 호출");
        long startTime = System.currentTimeMillis();

        String result = component.operation();

        long endTime = System.currentTimeMillis();
        log.info("TimeDecorator operation 종료");
        log.info("소요 시간 = {}ms", endTime - startTime);
        return result;
    }

}
