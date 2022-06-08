package com.roy.spring.advanced.trace;

import com.roy.spring.advanced.trace.mytrace.MyTraceVersion1;
import com.roy.spring.advanced.trace.mytrace.MyTraceVersion2;
import org.junit.jupiter.api.Test;

public class MyTraceVersion1Test {

    @Test
    void beginEnd() {
        MyTraceVersion1 myTraceVersion1 = new MyTraceVersion1();
        TraceStatus status = myTraceVersion1.begin("hello");
        myTraceVersion1.end(status);
    }

    @Test
    void beginException() {
        MyTraceVersion2 myTraceVersion2 = new MyTraceVersion2();
        TraceStatus status = myTraceVersion2.begin("hello");
        myTraceVersion2.exception(status, new IllegalStateException());
    }

}
