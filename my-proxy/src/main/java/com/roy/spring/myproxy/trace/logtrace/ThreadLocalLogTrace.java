package com.roy.spring.myproxy.trace.logtrace;

import com.roy.spring.myproxy.trace.TraceId;
import com.roy.spring.myproxy.trace.TraceStatus;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace extends LogTrace {

    private final ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder.get();
        long startTimeMillis = System.currentTimeMillis();
        log.info("[{}] {}{}",
            traceId.getId(),
            addSpace(START_PREFIX, traceId.getLevel()),
            message);
        return new TraceStatus(traceId, startTimeMillis, message);
    }

    private void syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (Objects.isNull(traceId)) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception exception) {
        complete(status, exception);
    }

    private void complete(TraceStatus status, Exception exception) {
        Long stopTimeMillis = System.currentTimeMillis();
        long resultTimeMillis = stopTimeMillis - status.getStartTimeMillis();
        TraceId traceId = status.getTraceId();
        if (Objects.isNull(exception)) {
            log.info("[{}] {}{} time = {}ms",
                traceId.getId(),
                addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                status.getMessage(),
                resultTimeMillis);
        } else {
            log.info("[{}] {}{} time = {}ms exception = {}",
                traceId.getId(),
                addSpace(EXCEPTION_PREFIX, traceId.getLevel()),
                status.getMessage(),
                resultTimeMillis,
                exception.toString());
        }
        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

}
