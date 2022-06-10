package com.roy.spring.advanced.trace.strategy;

import com.roy.spring.advanced.trace.strategy.strategy.ContextVersion2;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic1;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextVersion2Test {

    @Test
    void strategyVersion2Test() {
        ContextVersion2 context = new ContextVersion2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    @Test
    void strategyAnonymousTest() {
        ContextVersion2 context = new ContextVersion2();
        context.execute(new StrategyLogic() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 1 시작");
                log.info("==== 서비스 로직 1 작업");
                log.info("==== 서비스 로직 1 종료");
            }
        });
        context.execute(new StrategyLogic() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 2 시작");
                log.info("==== 서비스 로직 2 작업");
                log.info("==== 서비스 로직 2 종료");
            }
        });
    }

    @Test
    void strategyLambdaExpression() {
        ContextVersion2 context = new ContextVersion2();
        context.execute(() -> {
            log.info("==== 서비스 로직 1 시작");
            log.info("==== 서비스 로직 1 작업");
            log.info("==== 서비스 로직 1 종료");
        });
        context.execute(() -> {
            log.info("==== 서비스 로직 2 시작");
            log.info("==== 서비스 로직 2 작업");
            log.info("==== 서비스 로직 2 종료");
        });
    }

}
