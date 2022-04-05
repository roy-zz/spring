package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.domain.Order;
import com.roy.spring.repository.MemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.OrderService;
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
