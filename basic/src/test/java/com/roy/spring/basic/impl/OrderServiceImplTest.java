package com.roy.spring.basic.impl;

import static com.roy.spring.basic.enums.Grade.VIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.roy.spring.basic.configuration.PureApplicationConfig;
import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.domain.Order;
import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class OrderServiceImplTest {

    private final ApplicationContext applicationContext
            = new AnnotationConfigApplicationContext(PureApplicationConfig.class);

    private final MemberService memberService
            = applicationContext.getBean("memberService", MemberService.class);

    private final OrderService orderService
            = applicationContext.getBean("orderService", OrderService.class);

    @Test
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        // GIVEN
        long memberId = 0L;
        Member newMember = new Member(memberId, "Roy", VIP);
        memberService.signup(newMember);

        // WHEN
        Order order = orderService.createOrder(memberId, "MacbookPro", 10000);

        // THEN
        assertEquals(1000, order.getDiscountPrice());
    }

}