package com.roy.spring.advanced.trace;

import com.roy.spring.advanced.trace.mytrace.MyTraceVersion2;
import org.junit.jupiter.api.Test;

public class MyTraceVersion2Test {

    @Test
    void beginEnd() {
        MyTraceVersion2 myTraceVersion2 = new MyTraceVersion2();
        TraceStatus status1 = myTraceVersion2.begin("hello 1");
        TraceStatus status2 = myTraceVersion2.beginSync(status1.getTraceId(), "hello 2");
        myTraceVersion2.end(status2);
        myTraceVersion2.end(status1);
    }

    @Test
    void beginException() {
        MyTraceVersion2 myTraceVersion2 = new MyTraceVersion2();
        TraceStatus status1 = myTraceVersion2.begin("hello 1");
        TraceStatus status2 = myTraceVersion2.beginSync(status1.getTraceId(), "hello 2");
        myTraceVersion2.exception(status2, new IllegalStateException());
        myTraceVersion2.exception(status1, new IllegalStateException());
    }

}
