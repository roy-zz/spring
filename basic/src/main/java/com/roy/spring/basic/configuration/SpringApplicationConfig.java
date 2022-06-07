package com.roy.spring.basic.configuration;

import com.roy.spring.basic.repository.MemberRepository;
import com.roy.spring.basic.repository.impl.MemoryMemberRepository;
import com.roy.spring.basic.service.DiscountPolicy;
import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.service.OrderService;
import com.roy.spring.basic.service.impl.FixedDiscountPolicy;
import com.roy.spring.basic.service.impl.MemberServiceImpl;
import com.roy.spring.basic.service.impl.OrderServiceImpl;
import com.roy.spring.basic.service.impl.StatefulService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call orderService");
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call memberRepository");
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
