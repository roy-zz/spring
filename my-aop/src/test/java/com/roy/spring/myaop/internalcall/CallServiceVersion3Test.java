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
public class CallServiceVersion3Test {

    @Autowired
    private CallServiceVersion3 callServiceVersion3;

    @Test
    void external() {
        callServiceVersion3.external();
    }
}
