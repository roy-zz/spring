package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.repository.MemberRepository;
import com.roy.spring.repository.impl.MemoryMemberRepository;
import com.roy.spring.service.MemberService;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void signup(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
