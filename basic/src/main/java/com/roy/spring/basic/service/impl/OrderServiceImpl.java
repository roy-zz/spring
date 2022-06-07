package com.roy.spring.basic.service.impl;

import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.domain.Order;
import com.roy.spring.basic.repository.MemberRepository;
import com.roy.spring.basic.service.DiscountPolicy;
import com.roy.spring.basic.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;

    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member storedMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(storedMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
