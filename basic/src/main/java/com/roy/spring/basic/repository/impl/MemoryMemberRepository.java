package com.roy.spring.basic.repository.impl;

import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.repository.MemberRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

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
