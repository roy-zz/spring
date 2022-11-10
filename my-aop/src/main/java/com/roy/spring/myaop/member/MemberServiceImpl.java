package com.roy.spring.myaop.member;

import com.roy.spring.myaop.member.annotation.ClassAop;
import com.roy.spring.myaop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }
}
