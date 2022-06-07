package com.roy.spring.basic.service.impl;

import static com.roy.spring.basic.enums.Grade.VIP;

import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.service.DiscountPolicy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class RatioDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_RATIO = 10;

    @Override
    public int discount(Member member, int price) {
        return member.getGrade() == VIP ? price * DISCOUNT_RATIO / 100 : 0;
    }

}
