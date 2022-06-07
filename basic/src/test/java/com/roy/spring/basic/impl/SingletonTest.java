package com.roy.spring.basic.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.roy.spring.basic.configuration.PureApplicationConfig;
import com.roy.spring.basic.configuration.SpringApplicationConfig;
import com.roy.spring.basic.repository.MemberRepository;
import com.roy.spring.basic.service.MemberService;
import com.roy.spring.basic.service.impl.MemberServiceImpl;
import com.roy.spring.basic.service.impl.OrderServiceImpl;
import com.roy.spring.basic.service.impl.SingletonService;
import com.roy.spring.basic.service.impl.StatefulService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

    @Test
    @DisplayName("@Configuration 싱글톤 검증 테스트")
    void configurationSingletonValidateTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        System.out.println("memberService.getMemberRepository = " + memberService.getMemberRepository());
        System.out.println("orderService.getMemberRepository = " + orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);

        assertEquals(memberService.getMemberRepository(), memberRepository);
        assertEquals(orderService.getMemberRepository(), memberRepository);
    }

    @Test
    @DisplayName("SpringApplicationConfig 객체 확인 테스트")
    void configurationClassInstanceCheckTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        SpringApplicationConfig configBean = ac.getBean(SpringApplicationConfig.class);
        System.out.println("configBean = " + configBean);
    }

}
