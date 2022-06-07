package com.roy.spring.basic.impl;

import com.roy.spring.basic.service.impl.DBConnectionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class BeanLifecycleTest {
    @Test
    @DisplayName("빈 생명주기 테스트")
    void beanLifecycleTest() {
        ConfigurableApplicationContext cac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        DBConnectionService service = cac.getBean(DBConnectionService.class);
        cac.close();
    }

    @Configuration
    static class LifecycleConfig {
        @Bean
        public DBConnectionService dbConnectionService() {
            DBConnectionService dbConnectionService = new DBConnectionService();
            dbConnectionService.init("jdbc://com.roy.spring", "royId", "royPw");
            return dbConnectionService;
        }
    }
}
