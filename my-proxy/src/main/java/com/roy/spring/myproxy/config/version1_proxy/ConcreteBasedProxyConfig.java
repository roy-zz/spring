package com.roy.spring.myproxy.config.version1_proxy;

import com.roy.spring.myproxy.application.version2.OrderControllerVersion2;
import com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2;
import com.roy.spring.myproxy.application.version2.OrderServiceVersion2;
import com.roy.spring.myproxy.config.version1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import com.roy.spring.myproxy.config.version1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import com.roy.spring.myproxy.config.version1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import com.roy.spring.myproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteBasedProxyConfig {

    @Bean
    public OrderControllerVersion2 orderController(LogTrace logTrace) {
        OrderControllerVersion2 controllerImpl = new OrderControllerVersion2(orderService(logTrace));
        return new OrderControllerConcreteProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceVersion2 orderService(LogTrace logTrace) {
        OrderServiceVersion2 serviceImpl = new OrderServiceVersion2(orderRepository(logTrace));
        return new OrderServiceConcreteProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryVersion2 orderRepository(LogTrace logTrace) {
        OrderRepositoryVersion2 repositoryImpl = new OrderRepositoryVersion2();
        return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
