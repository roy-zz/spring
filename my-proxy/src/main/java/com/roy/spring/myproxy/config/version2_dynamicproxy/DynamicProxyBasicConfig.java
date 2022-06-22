package com.roy.spring.myproxy.config.version2_dynamicproxy;

import com.roy.spring.myproxy.application.version1.*;
import com.roy.spring.myproxy.config.version2_dynamicproxy.handler.LogTraceBasicHandler;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import com.roy.spring.myproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    public OrderControllerVersion1 orderController(LogTrace logTrace) {
        OrderControllerVersion1 orderController = new OrderControllerVersion1Impl(orderService(logTrace));
        return (OrderControllerVersion1) Proxy.newProxyInstance(
                OrderControllerVersion1.class.getClassLoader(),
                new Class[]{OrderControllerVersion1.class},
                new LogTraceBasicHandler(orderController, logTrace));
    }

    @Bean
    public OrderServiceVersion1 orderService(LogTrace logTrace) {
        OrderServiceVersion1 orderService = new OrderServiceVersion1Impl(orderRepository(logTrace));
        return (OrderServiceVersion1) Proxy.newProxyInstance(
                OrderServiceVersion1.class.getClassLoader(),
                new Class[]{OrderServiceVersion1.class},
                new LogTraceBasicHandler(orderService, logTrace));
    }

    @Bean
    public OrderRepositoryVersion1 orderRepository(LogTrace logTrace) {
        OrderRepositoryVersion1 orderRepository = new OrderRepositoryVersion1Impl();
        return (OrderRepositoryVersion1) Proxy.newProxyInstance(
                OrderRepositoryVersion1.class.getClassLoader(),
                new Class[]{OrderRepositoryVersion1.class},
                new LogTraceBasicHandler(orderRepository, logTrace)
        );
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
