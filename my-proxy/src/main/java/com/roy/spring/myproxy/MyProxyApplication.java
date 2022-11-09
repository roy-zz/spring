package com.roy.spring.myproxy;

import com.roy.spring.myproxy.config.version6_aop.AopConfig;
import com.roy.spring.myproxy.trace.logtrace.LogTrace;
import com.roy.spring.myproxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

// @Import({ ApplicationVersion1Config.class, ApplicationVersion2Config.class })
// @Import(InterfaceBasedProxyConfig.class)
// @Import(ConcreteBasedProxyConfig.class)
// @Import(DynamicProxyBasicConfig.class)
// @Import(ProxyFactoryConfigVersion2.class)
// @Import(BeanPostProcessorConfig.class)
// @Import(AutoProxyConfig.class)
@Import(AopConfig.class)
@SpringBootApplication(scanBasePackages = "com.roy.spring.myproxy")
public class MyProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProxyApplication.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
