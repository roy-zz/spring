package com.roy.spring.myproxy.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TraceStatus {
    private final TraceId traceId;
    private final Long startTimeMillis;
    private final String message;
}
