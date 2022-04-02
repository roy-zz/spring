package com.roy.spring.repository.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.repository.MemberRepository;

public class RDBMemberRepository implements MemberRepository {
    @Override
    public void save(Member member) {

    }

    @Override
    public Member findById(Long memberId) {
        return null;
    }
}
