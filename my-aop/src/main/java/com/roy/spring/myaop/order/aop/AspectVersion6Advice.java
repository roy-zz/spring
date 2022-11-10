package com.roy.spring.myaop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectVersion6Advice {

    @Around("com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // @Before
            log.info("[Transaction Start] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[Transaction Commit] {}", joinPoint.getSignature());
            return result;
        } catch (Exception exception) {
            // @AfterThrowing
            log.info("[Transaction Rollback] {}", joinPoint.getSignature());
            throw exception;
        } finally {
            // @After
            log.info("[Resource Release] {}", joinPoint.getSignature());
        }
    }

    @Before("com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {

        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doAfterReturn(JoinPoint joinPoint, Object result) {

        log.info("[return] {} return = {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {

        log.info("[exception] {}, message = {}", exception, exception.getMessage());
    }

    @After("com.roy.spring.myaop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {

        log.info("[after] {}", joinPoint.getSignature());
    }
}
