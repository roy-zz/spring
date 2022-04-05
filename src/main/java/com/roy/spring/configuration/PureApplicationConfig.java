package com.roy.spring.configuration;

import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.MemberService;
import com.roy.spring.service.OrderService;
import com.roy.spring.service.impl.FixedDiscountPolicy;
import com.roy.spring.service.impl.MemberServiceImpl;
import com.roy.spring.service.impl.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
