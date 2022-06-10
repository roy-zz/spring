package com.roy.spring.myproxy.config;

import com.roy.spring.myproxy.application.version2.OrderControllerVersion2;
import com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2;
import com.roy.spring.myproxy.application.version2.OrderServiceVersion2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationVersion2Config {

    @Bean
    public OrderControllerVersion2 orderControllerVersion2() {
        return new OrderControllerVersion2(orderServiceVersion2());
    }

    @Bean
    public OrderServiceVersion2 orderServiceVersion2() {
        return new OrderServiceVersion2(orderRepositoryVersion2());
    }

    @Bean
    public OrderRepositoryVersion2 orderRepositoryVersion2() {
        return new OrderRepositoryVersion2();
    }

}
