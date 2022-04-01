package com.roy.spring.repository;

import com.roy.spring.domain.Member;

public interface MemberRepository {
    void save(Member member);
    Member findById(Long memberId);
}
