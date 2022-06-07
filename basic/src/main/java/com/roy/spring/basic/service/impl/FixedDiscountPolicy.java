package com.roy.spring.basic.service.impl;

import static com.roy.spring.basic.enums.Grade.VIP;

import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.service.DiscountPolicy;
import org.springframework.stereotype.Component;

@Component
public class FixedDiscountPolicy implements DiscountPolicy {

    private static int DISCOUNT_FIX_AMOUNT = 1000;

    @Override
    public int discount(Member member, int price) {
        return member.getGrade() == VIP ? DISCOUNT_FIX_AMOUNT : 0;
    }

}
