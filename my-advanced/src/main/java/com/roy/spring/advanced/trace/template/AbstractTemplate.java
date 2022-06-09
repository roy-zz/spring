package com.roy.spring.advanced.trace.template;

import com.roy.spring.advanced.trace.TraceStatus;
import com.roy.spring.advanced.trace.logtrace.LogTrace;

public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin(message);
            T result = call();
            trace.end(status);
            return result;
        } catch (Exception exception) {
            trace.exception(status, exception);
            throw exception;
        }
    }

    protected abstract T call();

}
