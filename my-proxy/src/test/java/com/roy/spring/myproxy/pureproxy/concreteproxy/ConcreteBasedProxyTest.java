package com.roy.spring.myproxy.pureproxy.concreteproxy;

import com.roy.spring.myproxy.pureproxy.concreteproxy.code.ConcreteClient;
import com.roy.spring.myproxy.pureproxy.concreteproxy.code.ConcreteLogic;
import com.roy.spring.myproxy.pureproxy.concreteproxy.code.TimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteBasedProxyTest {

    @Test
    void notAppliedConcreteBasedTest() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    @Test
    void appliedConcreteBasedTest() {
        ConcreteLogic concreteLogic = new ConcreteLogic();
        TimeProxy timeProxy = new TimeProxy(concreteLogic);
        ConcreteClient client = new ConcreteClient(timeProxy);
        client.execute();
    }

}
