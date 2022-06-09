package com.roy.spring.advanced.trace.strategy;

import com.roy.spring.advanced.trace.strategy.strategy.ContextVersion1;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic1;
import com.roy.spring.advanced.trace.strategy.strategy.StrategyLogic2;
import org.junit.jupiter.api.Test;

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

}
