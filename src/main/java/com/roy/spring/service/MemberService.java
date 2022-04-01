package com.roy.spring.service;

import com.roy.spring.domain.Member;

public interface MemberService {
    void signup(Member member);
    Member findMember(Long memberId);
}
