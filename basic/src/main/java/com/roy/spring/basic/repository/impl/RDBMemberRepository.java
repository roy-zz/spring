package com.roy.spring.basic.repository.impl;

import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.repository.MemberRepository;

public class RDBMemberRepository implements MemberRepository {
    @Override
    public void save(Member member) {

    }

    @Override
    public Member findById(Long memberId) {
        return null;
    }
}
