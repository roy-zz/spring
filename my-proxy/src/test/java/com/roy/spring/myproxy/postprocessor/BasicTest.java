package com.roy.spring.myproxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {

        ApplicationContext context = new AnnotationConfigApplicationContext(BasicConfig.class);
        Roy roy = context.getBean("beanRoy", Roy.class);
        roy.helloRoy();

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> {
            context.getBean(Perry.class);
        });
    }

    @Slf4j
    @Configuration
    static class BasicConfig {

        @Bean(name = "beanRoy")
        public Roy roy() {

            return new Roy();
        }
    }

    @Slf4j
    static class Roy {

        public void helloRoy() {
            log.info("hello Roy");
        }
    }

    @Slf4j
    static class Perry {

        public void helloPerry() {
            log.info("hello Perry");
        }
    }
}
