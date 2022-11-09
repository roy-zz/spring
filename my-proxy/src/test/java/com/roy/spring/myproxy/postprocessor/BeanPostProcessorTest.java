package com.roy.spring.myproxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);
        Perry perry = context.getBean("beanRoy", Perry.class);
        perry.helloPerry();
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> {
            context.getBean(Roy.class);
        });
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {

        @Bean(name = "beanRoy")
        public Roy roy() {
            return new Roy();
        }

        @Bean
        public RoyToPerryPostProcessor helloPostProcessor() {
            return new RoyToPerryPostProcessor();
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

    @Slf4j
    static class RoyToPerryPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

            log.info("beanName = {}, bean = {}", beanName, bean);
            if (bean instanceof Roy) {
                return new Perry();
            }
            return bean;
        }
    }
}
