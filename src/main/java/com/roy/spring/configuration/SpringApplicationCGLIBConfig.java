package com.roy.spring.configuration;

import com.roy.spring.repository.MemberRepository;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

public class SpringApplicationCGLIBConfig extends SpringApplicationConfig {

    Map<String, Object> beans = new HashMap<>();

    @Bean
    @Override
    public MemberRepository memberRepository() {
        if (beans.containsKey("memberRepository")) {
            return (MemberRepository) beans.get("memberRepository");
        } else {
            MemberRepository memberRepository = super.memberRepository();
            beans.put("memberRepository", memberRepository);
            return memberRepository;
        }
    }

}
