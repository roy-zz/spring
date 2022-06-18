package com.roy.spring.myproxy.pureproxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageDecorator implements Component {

    private final Component component;

    @Override
    public String operation() {
        log.info("MessageDecorator operation 호출");
        String result = component.operation();
        String decoratedResult = "Decorated Result = " + result + "!!!";
        log.info("MessageDecorator 꾸미기 적용 전 = {}, 적용 후 = {}", result, decoratedResult);
        return decoratedResult;
    }

}
