package com.roy.spring.myaop.proxyvs;

import com.roy.spring.myaop.member.MemberService;
import com.roy.spring.myaop.member.MemberServiceImpl;
import com.roy.spring.myaop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ProxyDIAspect.class)
@SpringBootTest
// @SpringBootTest(properties = { "spring.aop.proxy-target-class=false" }) // JDK 동적 프록시
// @SpringBootTest(properties = { "spring.aop.proxy-target-class=true" }) // CGLIB 프록시
public class ProxyDITest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());
        log.info("memberServiceImpl class = {}", memberServiceImpl.getClass());
        memberServiceImpl.hello("helloRoy");
    }
}
