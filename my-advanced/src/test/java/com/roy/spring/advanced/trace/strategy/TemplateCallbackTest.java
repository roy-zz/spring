package com.roy.spring.advanced.trace.strategy;

import com.roy.spring.advanced.trace.strategy.template.MyCallback;
import com.roy.spring.advanced.trace.strategy.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

    @Test
    void callbackAnonymousClazz() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(new MyCallback() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 1 시작");
                log.info("==== 서비스 로직 1 작업");
                log.info("==== 서비스 로직 1 종료");
            }
        });
        template.execute(new MyCallback() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 2 시작");
                log.info("==== 서비스 로직 2 작업");
                log.info("==== 서비스 로직 2 종료");
            }
        });
    }

    @Test
    void callbackLambdaExpression() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> {
            log.info("==== 서비스 로직 1 시작");
            log.info("==== 서비스 로직 1 작업");
            log.info("==== 서비스 로직 1 종료");
        });

        template.execute(() -> {
            log.info("==== 서비스 로직 2 시작");
            log.info("==== 서비스 로직 2 작업");
            log.info("==== 서비스 로직 2 종료");
        });
    }

}
