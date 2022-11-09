package com.roy.spring.myproxy.config.version4_postprocessor;

import com.roy.spring.myproxy.config.ApplicationVersion1Config;
import com.roy.spring.myproxy.config.ApplicationVersion2Config;
import com.roy.spring.myproxy.config.version3_proxyfactory.advice.LogTraceAdvice;
import com.roy.spring.myproxy.config.version4_postprocessor.postprocessor.PackageLogTracePostProcessor;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({ApplicationVersion1Config.class, ApplicationVersion2Config.class})
public class BeanPostProcessorConfig {

    @Bean
    public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace) {

        return new PackageLogTracePostProcessor("com.roy.spring.myproxy", getAdvisor(logTrace));
    }

    private Advisor getAdvisor(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
