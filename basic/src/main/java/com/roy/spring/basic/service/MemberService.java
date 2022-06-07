package com.roy.spring.basic.service;

import com.roy.spring.basic.domain.Member;

public interface MemberService {
    void signup(Member member);
    Member findMember(Long memberId);
}
