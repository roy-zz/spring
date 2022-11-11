package com.roy.spring.myaop.internalcall;

import com.roy.spring.myaop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(CallLogAspect.class)
public class CallServiceVersion2Test {

    @Autowired
    private CallServiceVersion2 callServiceVersion2;

    @Test
    void external() {
        callServiceVersion2.external();
    }
}
