package com.roy.spring.service.impl.config;

import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.impl.FixedDiscountPolicy;
import com.roy.spring.service.impl.RatioDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public MemberRepository redisMemberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public MemberRepository memcachedMemberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy ratioDiscountPolicy() {
        return new RatioDiscountPolicy();
    }

    @Bean
    public DiscountPolicy fixedDiscountPolicy() {
        return new FixedDiscountPolicy();
    }

}
