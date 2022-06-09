package com.roy.spring.advanced.trace.logtrace;

import com.roy.spring.advanced.trace.TraceId;
import com.roy.spring.advanced.trace.TraceStatus;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace extends LogTrace {
    private TraceId traceIdHolder;

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder;
        long startTimeMillis = System.currentTimeMillis();
        log.info("[{}] {}{}",
            traceId.getId(),
            addSpace(START_PREFIX, traceId.getLevel()),
            message);
        return new TraceStatus(traceId, startTimeMillis, message);
    }

    private void syncTraceId() {
        if (Objects.isNull(traceIdHolder)) {
            traceIdHolder = new TraceId();
        } else {
            traceIdHolder = traceIdHolder.createNextId();
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
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null;
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

}
