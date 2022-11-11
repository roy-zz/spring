package com.roy.spring.myaop.internalcall;

import com.roy.spring.myaop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({CallLogAspect.class})
public class CallServiceVersion1Test {

    @Autowired
    private CallServiceVersion1 callServiceVersion1;

    @Test
    void external() {
        callServiceVersion1.external();
    }
}
