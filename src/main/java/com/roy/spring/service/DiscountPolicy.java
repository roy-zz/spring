package com.roy.spring.service;

import com.roy.spring.domain.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
