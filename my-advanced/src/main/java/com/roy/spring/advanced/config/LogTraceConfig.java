package com.roy.spring.advanced.config;

import com.roy.spring.advanced.trace.logtrace.FieldLogTrace;
import com.roy.spring.advanced.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logTrace() {
        return new FieldLogTrace();
    }
}
