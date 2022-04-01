package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.roy.spring.enums.Grade.BASIC;
import static com.roy.spring.enums.Grade.VIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RatioDiscountPolicyTest {

    private final RatioDiscountPolicy discountPolicy = new RatioDiscountPolicy();

    @Test
    @DisplayName("VIP인 회원 정률 할인 적용 테스트")
    void vipAppliedDiscountTest() {
        // GIVEN
        Member vipMember = new Member(0L, "VIP", VIP);

        // WHEN
        int discountCost = discountPolicy.discount(vipMember, 10000);

        // THEN
        assertEquals(1000, discountCost);
    }

    @Test
    @DisplayName("VIP가 아닌 회원 정률 할인 미적용 테스트")
    void nonVipNotAppliedDiscountTest() {
        // GIVEN
        Member nonVipMember = new Member(0L, "NON-VIP", BASIC);

        // WHEN
        int discountCost = discountPolicy.discount(nonVipMember, 10000);

        // THEN
        assertEquals(0, discountCost);
    }

}