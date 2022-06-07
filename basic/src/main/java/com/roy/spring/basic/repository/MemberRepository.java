package com.roy.spring.basic.repository;

import com.roy.spring.basic.domain.Member;

public interface MemberRepository {
    void save(Member member);
    Member findById(Long memberId);
}
