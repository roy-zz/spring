package com.roy.spring.myproxy.cglib;

import com.roy.spring.myproxy.cglib.code.TimeMethodInterceptor;
import com.roy.spring.myproxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglibTest() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("target class = {}", target.getClass());
        log.info("proxy class = {}", proxy.getClass());

        proxy.call();
    }

}
