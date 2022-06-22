package com.roy.spring.myproxy.jdkdynamic;

import com.roy.spring.myproxy.jdkdynamic.code.PerryImpl;
import com.roy.spring.myproxy.jdkdynamic.code.PerryInterface;
import com.roy.spring.myproxy.jdkdynamic.code.RoyImpl;
import com.roy.spring.myproxy.jdkdynamic.code.RoyInterface;
import com.roy.spring.myproxy.jdkdynamic.code.TimeInvocationHandler;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicRoy() {
        RoyInterface target = new RoyImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        RoyInterface proxy = (RoyInterface) Proxy.newProxyInstance(
            RoyInterface.class.getClassLoader(), new Class[] {RoyInterface.class}, handler);
        proxy.call();
        log.info("Target class = {}", target.getClass());
        log.info("Proxy class = {}", proxy.getClass());
    }

    @Test
    void dynamicPerry() {
        PerryInterface target = new PerryImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        PerryInterface proxy = (PerryInterface) Proxy.newProxyInstance(
            PerryInterface.class.getClassLoader(), new Class[] {PerryInterface.class}, handler);
        proxy.call();
        log.info("Target class = {}", target.getClass());
        log.info("Proxy class = {}", proxy.getClass());
    }

}
