package com.roy.spring.advanced.trace.strategy.strategy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContextVersion2 {

    public void execute(StrategyLogic strategyLogic) {
        long startTime = System.currentTimeMillis();

        strategyLogic.call();

        long endTime = System.currentTimeMillis();
        log.info("spent = {}", endTime - startTime);
    }

}
