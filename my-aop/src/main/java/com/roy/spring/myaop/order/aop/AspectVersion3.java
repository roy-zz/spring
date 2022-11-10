package com.roy.spring.myaop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectVersion3 {

    @Pointcut("execution(* com.roy.spring.myaop.order..*(..))")
    private void allOrder() {
    }

    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {
    }

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    @Around("allOrder() && allService()")
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
