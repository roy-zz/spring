package com.roy.spring.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.junit.jupiter.api.Assertions.*;

public class BeanScopeTest {

    @Test
    @DisplayName("싱글톤 vs 프로토타입 테스트")
    public void beanScopeTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class, PrototypeBean.class);
        SingletonBean sb1 = ac.getBean(SingletonBean.class);
        SingletonBean sb2 = ac.getBean(SingletonBean.class);
        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);
        System.out.println("sb1 = " + sb1);
        System.out.println("sb2 = " + sb2);
        System.out.println("pb1 = " + pb1);
        System.out.println("pb2 = " + pb2);
        assertEquals(sb1, sb2);
        assertNotEquals(pb1, pb2);
        ac.close();
    }

    @Test
    @DisplayName("프로토타입 필드 변수 확인 테스트")
    void prototypeFieldVariableCheckTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);
        pb1.addCount();
        assertEquals(1, pb1.getCount());
        pb2.addCount();
        assertEquals(1, pb2.getCount());
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;
        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("Initialized Prototype");
        }
        @PreDestroy
        public void destroy() {
            System.out.println("Destroy Prototype");
        }
    }

    @Scope("singleton")
    static class SingletonBean {
        private final PrototypeBean prototypeBean;
        public SingletonBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }
        public int calculatePrototypeCount() {
            prototypeBean.addCount();;
            return prototypeBean.getCount();
        }
    }

}
