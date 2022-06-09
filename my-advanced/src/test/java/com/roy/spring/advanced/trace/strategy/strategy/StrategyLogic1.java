package com.roy.spring.advanced.trace.strategy.strategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic1 implements StrategyLogic {
    @Override
    public void call() {
        log.info("==== 서비스 로직 1 시작");
        log.info("==== 서비스 로직 1 작업");
        log.info("==== 서비스 로직 1 종료");
    }
}
