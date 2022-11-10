package com.roy.spring.myaop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectVersion4Pointcut {

    @Around("com.roy.spring.myaop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    @Around("com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            log.info("[Transaction Start] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[Transaction Commit] {}", joinPoint.getSignature());
            return result;
        } catch (Exception exception) {
            log.info("[Transaction Rollback] {}", joinPoint.getSignature());
            throw exception;
        } finally {
            log.info("[Resource Release] {}", joinPoint.getSignature());
        }
    }
}
