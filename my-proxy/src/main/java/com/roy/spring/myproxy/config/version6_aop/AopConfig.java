package com.roy.spring.myproxy.config.version6_aop;

import com.roy.spring.myproxy.config.ApplicationVersion1Config;
import com.roy.spring.myproxy.config.ApplicationVersion2Config;
import com.roy.spring.myproxy.config.version6_aop.aspect.LogTraceAspect;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ApplicationVersion1Config.class, ApplicationVersion2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {

        return new LogTraceAspect(logTrace);
    }
}
