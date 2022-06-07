package com.roy.spring.basic.impl;

import static com.roy.spring.basic.enums.Grade.VIP;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.roy.spring.basic.configuration.PureApplicationConfig;
import com.roy.spring.basic.domain.Member;
import com.roy.spring.basic.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class MemberServiceImplTest {

    private final ApplicationContext applicationContext
            = new AnnotationConfigApplicationContext(PureApplicationConfig.class);

    private final MemberService memberService
            = applicationContext.getBean("memberService", MemberService.class);

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