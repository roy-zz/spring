package com.roy.spring.myproxy.config.version3_proxyfactory;

import com.roy.spring.myproxy.application.version2.OrderControllerVersion2;
import com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2;
import com.roy.spring.myproxy.application.version2.OrderServiceVersion2;
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
public class ProxyFactoryConfigVersion2 {

    @Bean
    public OrderControllerVersion2 orderControllerVersion2(LogTrace logTrace) {

        OrderControllerVersion2 orderController = new OrderControllerVersion2(orderServiceVersion2(logTrace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerVersion2 proxy = (OrderControllerVersion2) factory.getProxy();
        log.info("ProxyFactory Controller proxy = {}, target = {}", proxy.getClass(), orderController.getClass());

        return proxy;
    }

    @Bean
    public OrderServiceVersion2 orderServiceVersion2(LogTrace logTrace) {

        OrderServiceVersion2 orderService = new OrderServiceVersion2(orderRepositoryVersion2(logTrace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceVersion2 proxy = (OrderServiceVersion2) factory.getProxy();
        log.info("ProxyFactory Service proxy = {}, target = {}", proxy.getClass(), orderService.getClass());

        return proxy;
    }

    @Bean
    public OrderRepositoryVersion2 orderRepositoryVersion2(LogTrace logTrace) {

        OrderRepositoryVersion2 orderRepository = new OrderRepositoryVersion2();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryVersion2 proxy = (OrderRepositoryVersion2) factory.getProxy();
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
