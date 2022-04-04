package com.roy.spring.service.impl;

import com.roy.spring.configuration.ApplicationConfig;
import com.roy.spring.domain.Member;
import com.roy.spring.domain.Order;
import com.roy.spring.service.MemberService;
import com.roy.spring.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.roy.spring.enums.Grade.VIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceImplTest {

    private final ApplicationContext applicationContext
            = new AnnotationConfigApplicationContext(ApplicationConfig.class);

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