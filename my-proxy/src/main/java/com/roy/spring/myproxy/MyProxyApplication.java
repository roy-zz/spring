package com.roy.spring.myproxy;

import com.roy.spring.myproxy.config.version1_proxy.ConcreteBasedProxyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

// @Import({ ApplicationVersion1Config.class, ApplicationVersion2Config.class })
// @Import(InterfaceBasedProxyConfig.class)
@Import(ConcreteBasedProxyConfig.class)
@SpringBootApplication(scanBasePackages = "com.roy.spring.myproxy.application")
public class MyProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProxyApplication.class, args);
    }

}
