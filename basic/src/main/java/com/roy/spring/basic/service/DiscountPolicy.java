package com.roy.spring.basic.service;

import com.roy.spring.basic.domain.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
