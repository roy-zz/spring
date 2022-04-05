package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.service.DiscountPolicy;
import org.springframework.stereotype.Component;

import static com.roy.spring.enums.Grade.VIP;

@Component
public class RatioDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_RATIO = 10;

    @Override
    public int discount(Member member, int price) {
        return member.getGrade() == VIP ? price * DISCOUNT_RATIO / 100 : 0;
    }

}
