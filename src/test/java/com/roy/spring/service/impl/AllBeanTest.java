package com.roy.spring.service.impl;

import com.roy.spring.configuration.AutoSpringApplicationConfig;
import com.roy.spring.domain.Member;
import com.roy.spring.enums.Grade;
import com.roy.spring.service.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllBeanTest {

    @Test
    @DisplayName("동일 타입 다중 빈 검색 테스트")
    void findAllBeanTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(
                AutoSpringApplicationConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(0L, "Roy", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixedDiscountPolicy");

        assertTrue(discountService instanceof DiscountService);
        assertEquals(1000, discountPrice);
    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> mapOfPolicies;
        private final List<DiscountPolicy> listOfPolicies;

        public DiscountService(Map<String, DiscountPolicy> mapOfPolicies, List<DiscountPolicy> listOfPolicies) {
            this.mapOfPolicies = mapOfPolicies;
            this.listOfPolicies = listOfPolicies;
            System.out.println("mapOfPolicies = " + mapOfPolicies);
            System.out.println("listOfPolicies = " + listOfPolicies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = mapOfPolicies.get(discountCode);
            System.out.println("discountCode = " + discountCode);
            System.out.println("discountPolicy = " + discountPolicy);
            return discountPolicy.discount(member, price);
        }
    }
}
