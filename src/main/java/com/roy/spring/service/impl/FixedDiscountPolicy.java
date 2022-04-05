package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.service.DiscountPolicy;
import org.springframework.stereotype.Component;

import static com.roy.spring.enums.Grade.VIP;

@Component
public class FixedDiscountPolicy implements DiscountPolicy {

    private static int DISCOUNT_FIX_AMOUNT = 1000;

    @Override
    public int discount(Member member, int price) {
        return member.getGrade() == VIP ? DISCOUNT_FIX_AMOUNT : 0;
    }

}
