package com.roy.spring.advanced.trace.template;

import com.roy.spring.advanced.trace.template.code.AbstractTemplate;
import com.roy.spring.advanced.trace.template.code.SubClassLogic1;
import com.roy.spring.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethod() {
        serviceLogic1();
        serviceLogic2();
    }

    private void serviceLogic1() {
        long startTime = System.currentTimeMillis();
        log.info("==== 서비스 로직 1 시작");
        log.info("==== 서비스 로직 1 작업");
        log.info("==== 서비스 로직 1 종료");
        long endTime = System.currentTimeMillis();
        log.info("spent = {}", endTime - startTime);
    }

    private void serviceLogic2() {
        long startTime = System.currentTimeMillis();
        log.info("==== 서비스 로직 2 시작");
        log.info("==== 서비스 로직 2 작업");
        log.info("==== 서비스 로직 2 종료");
        long endTime = System.currentTimeMillis();
        log.info("spent = {}", endTime - startTime);
    }

    @Test
    void applyTemplateMethodExampleTest() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }

    @Test
    void templateMethodWithAnonymousClazzTest() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("==== 서비스 로직 1 시작");
                log.info("==== 서비스 로직 1 작업");
                log.info("==== 서비스 로직 1 종료");
            }
        };

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("==== 서비스 로직 2 시작");
                log.info("==== 서비스 로직 2 작업");
                log.info("==== 서비스 로직 2 종료");
            }
        };

        template1.execute();
        template2.execute();

    }

}
