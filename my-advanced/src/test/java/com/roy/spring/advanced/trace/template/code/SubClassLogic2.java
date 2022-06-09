package com.roy.spring.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic2 extends AbstractTemplate {
    @Override
    protected void call() {
        log.info("==== 서비스 로직 2 시작");
        log.info("==== 서비스 로직 2 작업");
        log.info("==== 서비스 로직 2 종료");
    }
}
