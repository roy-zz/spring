package com.roy.spring.myproxy.config.version6_aop.aspect;

import com.roy.spring.myproxy.trace.TraceStatus;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {

        this.logTrace = logTrace;
    }

    @Around("execution(* com.roy.spring.myproxy..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }
}
