package com.roy.spring.service.impl;

import com.roy.spring.domain.Member;
import com.roy.spring.enums.Grade;
import com.roy.spring.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.roy.spring.enums.Grade.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceImplTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() {
        // GIVEN
        Member member = new Member(0L, "Roy", VIP);

        // WHEN
        memberService.signup(member);
        Member storedMember = memberService.findMember(0L);

        // THEN
        assertEquals(member, storedMember);
    }

}