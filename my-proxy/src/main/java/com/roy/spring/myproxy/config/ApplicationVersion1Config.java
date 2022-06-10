package com.roy.spring.myproxy.config;

import com.roy.spring.myproxy.application.version1.OrderControllerVersion1;
import com.roy.spring.myproxy.application.version1.OrderControllerVersion1Impl;
import com.roy.spring.myproxy.application.version1.OrderRepositoryVersion1;
import com.roy.spring.myproxy.application.version1.OrderRepositoryVersion1Impl;
import com.roy.spring.myproxy.application.version1.OrderServiceVersion1;
import com.roy.spring.myproxy.application.version1.OrderServiceVersion1Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationVersion1Config {

    @Bean
    public OrderControllerVersion1 orderControllerVersion1() {
        return new OrderControllerVersion1Impl(orderServiceVersion1());
    }

    @Bean
    public OrderServiceVersion1 orderServiceVersion1() {
        return new OrderServiceVersion1Impl(orderRepositoryVersion1());
    }

    @Bean
    public OrderRepositoryVersion1 orderRepositoryVersion1() {
        return new OrderRepositoryVersion1Impl();
    }

}
