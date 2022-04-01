package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.domain.Order;
import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.DiscountPolicy;
import com.roy.spring.service.OrderService;

public class OrderServiceImpl implements OrderService {

    private final DiscountPolicy discountPolicy = new FixedDiscountPolicy();
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member storedMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(storedMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

}
