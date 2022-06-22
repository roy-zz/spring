package com.roy.spring.myproxy.trace.logtrace;

import com.roy.spring.myproxy.trace.TraceStatus;

public abstract class LogTrace {
    protected static final String START_PREFIX = "-->";
    protected static final String COMPLETE_PREFIX = "<--";
    protected static final String EXCEPTION_PREFIX = "<X-";

    public abstract TraceStatus begin(String message);
    public abstract void end(TraceStatus status);
    public abstract void exception(TraceStatus status, Exception exception);

    protected String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

}
