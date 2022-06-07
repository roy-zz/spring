package com.roy.spring.basic.configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AutoSpringApplicationConfigTest {

    @Test
    @DisplayName("컴포넌트 스캔을 통한 자동 의존성 주입 테스트")
    void diViaComponentScanTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoSpringApplicationConfig.class);
         MemberService memberService = ac.getBean(MemberService.class);
         assertTrue(memberService instanceof MemberServiceImpl);
    }

}