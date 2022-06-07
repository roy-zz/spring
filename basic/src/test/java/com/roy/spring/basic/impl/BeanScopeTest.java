package com.roy.spring.basic.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

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

    @Test
    @DisplayName("싱글톤타입 & 프로토타입 동시 사용 테스트")
    void singletonAndPrototypeTest() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(SingletonBean.class, PrototypeBean.class);

        SingletonBean sb1 = ac.getBean(SingletonBean.class);
        int countForUser1 = sb1.calculatePrototypeCount();
        assertEquals(1, countForUser1);

        SingletonBean sb2 = ac.getBean(SingletonBean.class);
        int countForUser2 = sb2.calculatePrototypeCount();
        assertEquals(1, countForUser2);
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
        private final Provider<PrototypeBean> provider;
        public SingletonBean(Provider<PrototypeBean> provider) {
            this.provider = provider;
        }
        public int calculatePrototypeCount() {
            PrototypeBean prototypeBean = provider.get();
            prototypeBean.addCount();;
            return prototypeBean.getCount();
        }
    }

}
