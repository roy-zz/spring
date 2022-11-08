package com.roy.spring.myproxy.config.version3_proxyfactory;

import com.roy.spring.myproxy.application.version1.*;
import com.roy.spring.myproxy.config.version3_proxyfactory.advice.LogTraceAdvice;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigVersion1 {

    @Bean
    public OrderControllerVersion1 orderControllerVersion1(LogTrace logTrace) {

        OrderControllerVersion1 orderController = new OrderControllerVersion1Impl(orderServiceVersion1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerVersion1 proxy = (OrderControllerVersion1) factory.getProxy();
        log.info("ProxyFactory Controller proxy = {}, target = {}", proxy.getClass(), orderController.getClass());

        return proxy;
    }

    @Bean
    public OrderServiceVersion1 orderServiceVersion1(LogTrace logTrace) {

        OrderServiceVersion1 orderService = new OrderServiceVersion1Impl(orderRepositoryVersion1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceVersion1 proxy = (OrderServiceVersion1) factory.getProxy();
        log.info("ProxyFactory Service proxy = {}, target = {}", proxy.getClass(), orderService.getClass());

        return proxy;
    }

    @Bean
    public OrderRepositoryVersion1 orderRepositoryVersion1(LogTrace logTrace) {

        OrderRepositoryVersion1Impl orderRepository = new OrderRepositoryVersion1Impl();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryVersion1 proxy = (OrderRepositoryVersion1) factory.getProxy();
        log.info("ProxyFactory Repository proxy = {}, target = {}", proxy.getClass(), orderRepository.getClass());

        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
