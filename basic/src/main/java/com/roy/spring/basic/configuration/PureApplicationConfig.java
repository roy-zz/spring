package com.roy.spring.basic.configuration;

import com.roy.spring.basic.repository.MemberRepository;
import com.roy.spring.basic.repository.impl.MemoryMemberRepository;
import com.roy.spring.basic.service.DiscountPolicy;
import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.service.OrderService;
import com.roy.spring.basic.service.impl.FixedDiscountPolicy;
import com.roy.spring.basic.service.impl.MemberServiceImpl;
import com.roy.spring.basic.service.impl.OrderServiceImpl;

public class PureApplicationConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }

}
