package com.roy.spring.repository.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.repository.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> STORAGE = new ConcurrentHashMap<>();

    @Override
    public void save(Member member) {
        STORAGE.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return STORAGE.get(memberId);
    }

}
