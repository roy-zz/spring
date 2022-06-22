package com.roy.spring.myproxy.trace.callback;

public interface TraceCallback<T> {
    T call();
}
