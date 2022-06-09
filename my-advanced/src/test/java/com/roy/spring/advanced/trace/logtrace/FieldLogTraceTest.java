package com.roy.spring.advanced.trace.logtrace;

import com.roy.spring.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

public class FieldLogTraceTest {

    FieldLogTrace trace = new FieldLogTrace();

    @Test
    void beginEndTest() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void beginExceptionTest() {
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }

}
