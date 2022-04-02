package com.roy.spring.configuration;

import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.RDBMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.MemberService;
import com.roy.spring.service.OrderService;
import com.roy.spring.service.impl.MemberServiceImpl;
import com.roy.spring.service.impl.OrderServiceImpl;
import com.roy.spring.service.impl.RatioDiscountPolicy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {

    public static final AppConfig APP_CONFIG = new AppConfig();

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    private MemberRepository memberRepository() {
        // return new MemoryMemberRepository();
        return new RDBMemberRepository();
    }

    private DiscountPolicy discountPolicy() {
        // return new FixedDiscountPolicy();
        return new RatioDiscountPolicy();
    }

}
