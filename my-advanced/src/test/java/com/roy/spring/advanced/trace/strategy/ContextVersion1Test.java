package com.roy.spring.advanced.trace.strategy;

import com.roy.spring.advanced.trace.strategy.strategy.ContextVersion1;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic1;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextVersion1Test {

    @Test
    void strategyVersion1Test() {
        StrategyLogic strategyLogic1 = new StrategyLogic1();
        ContextVersion1 context1 = new ContextVersion1(strategyLogic1);
        context1.execute();

        StrategyLogic strategyLogic2 = new StrategyLogic2();
        ContextVersion1 context2 = new ContextVersion1(strategyLogic2);
        context2.execute();
    }

    @Test
    void strategyAndAnonymousClazz() {
        StrategyLogic strategyLogic1 = new StrategyLogic() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 1 시작");
                log.info("==== 서비스 로직 1 작업");
                log.info("==== 서비스 로직 1 종료");
            }
        };
        ContextVersion1 context1 = new ContextVersion1(strategyLogic1);
        context1.execute();

        StrategyLogic strategyLogic2 = new StrategyLogic() {
            @Override
            public void call() {
                log.info("==== 서비스 로직 2 시작");
                log.info("==== 서비스 로직 2 작업");
                log.info("==== 서비스 로직 2 종료");
            }
        };
        ContextVersion1 context2 = new ContextVersion1(strategyLogic2);
        context2.execute();
    }

    @Test
    void strategyAndLambdaExpression() {
        ContextVersion1 context1 = new ContextVersion1(() -> {
            log.info("==== 서비스 로직 1 시작");
            log.info("==== 서비스 로직 1 작업");
            log.info("==== 서비스 로직 1 종료");
        });
        ContextVersion1 context2 = new ContextVersion1(() -> {
            log.info("==== 서비스 로직 2 시작");
            log.info("==== 서비스 로직 2 작업");
            log.info("==== 서비스 로직 2 종료");
        });
        context1.execute();
        context2.execute();
    }

}
