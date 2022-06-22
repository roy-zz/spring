package com.roy.spring.myproxy.config.version1_proxy;

import com.roy.spring.myproxy.application.version1.OrderControllerVersion1;
import com.roy.spring.myproxy.application.version1.OrderControllerVersion1Impl;
import com.roy.spring.myproxy.application.version1.OrderRepositoryVersion1;
import com.roy.spring.myproxy.application.version1.OrderRepositoryVersion1Impl;
import com.roy.spring.myproxy.application.version1.OrderServiceVersion1;
import com.roy.spring.myproxy.application.version1.OrderServiceVersion1Impl;
import com.roy.spring.myproxy.config.version1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import com.roy.spring.myproxy.config.version1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import com.roy.spring.myproxy.config.version1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import com.roy.spring.myproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceBasedProxyConfig {

    @Bean
    public OrderControllerVersion1 orderController(LogTrace logTrace) {
        OrderControllerVersion1Impl controllerImpl = new OrderControllerVersion1Impl(orderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceVersion1 orderService(LogTrace logTrace) {
        OrderServiceVersion1Impl serviceImpl = new OrderServiceVersion1Impl(orderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryVersion1 orderRepository(LogTrace logTrace) {
        OrderRepositoryVersion1Impl repositoryImpl = new OrderRepositoryVersion1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

}
