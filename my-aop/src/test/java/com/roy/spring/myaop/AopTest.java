package com.roy.spring.myaop;

import com.roy.spring.myaop.order.OrderRepository;
import com.roy.spring.myaop.order.OrderService;
import com.roy.spring.myaop.order.aop.AspectVersion6Advice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
// @Import(AspectVersion1.class)
// @Import(AspectVersion2.class)
// @Import(AspectVersion3.class)
// @Import(AspectVersion4Pointcut.class)
// @Import({ AspectVersion5Order.LogAspect.class, AspectVersion5Order.TxAspect.class })
@Import(AspectVersion6Advice.class)
public class AopTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void aopInfo() {

        log.info("isAopProxy, orderService = {}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository = {}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void successTest() {

        orderService.orderItem("itemRoy");
    }

    @Test
    void exceptionTest() {
        Assertions.assertThatThrownBy(() -> orderService.orderItem("exception"))
                .isInstanceOf(IllegalStateException.class);
    }
}
