package com.roy.spring.configuration;

import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.MemberService;
import com.roy.spring.service.OrderService;
import com.roy.spring.service.impl.FixedDiscountPolicy;
import com.roy.spring.service.impl.MemberServiceImpl;
import com.roy.spring.service.impl.OrderServiceImpl;
import com.roy.spring.service.impl.StatefulService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }

    @Bean
    public StatefulService statefulService() {
        return new StatefulService();
    }

}
