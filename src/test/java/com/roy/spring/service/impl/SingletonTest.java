package com.roy.spring.service.impl;

import com.roy.spring.configuration.PureApplicationConfig;
import com.roy.spring.configuration.SpringApplicationConfig;
import com.roy.spring.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    @DisplayName("순수 자바 DI 컨테이너 Non Singleton 테스트")
    void pureJavaDiContainerNonSingletonTest() {
        PureApplicationConfig pureApplicationConfig = new PureApplicationConfig();

        MemberService memberService1 = pureApplicationConfig.memberService();
        MemberService memberService2 = pureApplicationConfig.memberService();

        assertNotEquals(memberService1, memberService2);
    }

    @Test
    @DisplayName("싱글톤 서비스 고유 객체 확인 테스트")
    void singletonServiceUniqueInstanceTest() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();
        assertEquals(singletonService1, singletonService2);
    }

    @Test
    @DisplayName("싱글톤 컨테이너 고유 객체 확인 테스트")
    void springContainerSingletonTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        assertEquals(memberService1, memberService2);
    }

    @Test
    @DisplayName("싱글톤 필드 공유 오류 발생 확인 테스트")
    void singletonSharingFieldOccurExceptionTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        statefulService1.order("Roy", 10000);
        statefulService2.order("Perry", 20000);

        assertEquals(10000, statefulService1.getPrice());
    }

}
