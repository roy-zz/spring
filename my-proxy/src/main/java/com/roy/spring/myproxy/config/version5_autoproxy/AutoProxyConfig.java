package com.roy.spring.myproxy.config.version5_autoproxy;

import com.roy.spring.myproxy.config.ApplicationVersion1Config;
import com.roy.spring.myproxy.config.ApplicationVersion2Config;
import com.roy.spring.myproxy.config.version3_proxyfactory.advice.LogTraceAdvice;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ApplicationVersion1Config.class, ApplicationVersion2Config.class})
public class AutoProxyConfig {

    // @Bean
    public Advisor advisorOne(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // @Bean
    public Advisor advisorTwo(LogTrace logTrace) {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.roy.spring.myproxy..*(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisorThree(LogTrace logTrace) {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.roy.spring.myproxy..*(..)) && !execution(* com.roy.spring.myproxy..noLog(..))");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
