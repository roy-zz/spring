package com.roy.spring.myproxy.proxy.code;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CacheProxy implements Subject {

    private final Subject target;
    private String cachedValue;

    @Override
    public String operation() {
        log.info("프록시 객체 호출");
        if (Objects.isNull(cachedValue)) {
            cachedValue = target.operation();
        }
        return cachedValue;
    }

}
