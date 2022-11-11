package com.roy.spring.myaop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceVersion1 {

    private CallServiceVersion1 callServiceVersion1;

    @Autowired
    public void setCallServiceVersion1(@Lazy CallServiceVersion1 callServiceVersion1) {
        this.callServiceVersion1 = callServiceVersion1;
    }

    public void external() {
        log.info("call external");
        callServiceVersion1.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
