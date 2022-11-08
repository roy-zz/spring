package com.roy.spring.myproxy.advisor;

import com.roy.spring.myproxy.common.service.ServiceImpl;
import com.roy.spring.myproxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class MultiAdvisorTest {

    @Test
    @DisplayName("여러 개의 프록시를 통한 다중 어드바이저")
    void multiAdvisorTestOne() {

        // Process
        // client -> proxy two(advisor two) -> proxy one(advisor one) -> target

        // 프록시 One 생성
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactoryOne = new ProxyFactory(target);
        DefaultPointcutAdvisor advisorOne = new DefaultPointcutAdvisor(Pointcut.TRUE, new AdviceOne());
        proxyFactoryOne.addAdvisor(advisorOne);
        ServiceInterface proxyOne = (ServiceInterface) proxyFactoryOne.getProxy();

        // 프록시 Two 생성, target -> proxy one 입력
        ProxyFactory proxyFactoryTwo = new ProxyFactory(proxyOne);
        DefaultPointcutAdvisor advisorTwo = new DefaultPointcutAdvisor(Pointcut.TRUE, new AdviceTwo());
        proxyFactoryTwo.addAdvisor(advisorTwo);
        ServiceInterface proxyTwo = (ServiceInterface) proxyFactoryTwo.getProxy();

        proxyTwo.save();
    }

    @Test
    @DisplayName("하나의 프록시를 통한 다중 어드바이저")
    void multiAdvisorTestTwo() {

        // Process
        // proxy -> advisorTwo -> advisorOne -> target

        DefaultPointcutAdvisor advisorOne = new DefaultPointcutAdvisor(Pointcut.TRUE, new AdviceOne());
        DefaultPointcutAdvisor advisorTwo = new DefaultPointcutAdvisor(Pointcut.TRUE, new AdviceTwo());

        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvisor(advisorTwo);
        proxyFactory.addAdvisor(advisorOne);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
    }

    @Slf4j
    static class AdviceOne implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice one 호출");
            return invocation.proceed();
        }
    }

    @Slf4j
    static class AdviceTwo implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice two 호출");
            return invocation.proceed();
        }
    }
}
