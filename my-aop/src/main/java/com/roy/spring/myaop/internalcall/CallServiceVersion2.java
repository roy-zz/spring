package com.roy.spring.myaop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceVersion2 {

    private final ObjectProvider<CallServiceVersion2> callServiceProvider;

    public void external() {
        log.info("call external");
        CallServiceVersion2 callServiceVersion2 = callServiceProvider.getObject();
        callServiceVersion2.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
