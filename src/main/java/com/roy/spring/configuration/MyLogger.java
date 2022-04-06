package com.roy.spring.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Slf4j
@Setter
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestUrl;

    public void print(String message) {
        log.info("[{}] [{}] {}", uuid, requestUrl, message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        log.info("[{}] Created request scope bean: {}", uuid, this);
    }
    @PreDestroy
    public void destroy() {
        log.info("[{}] Destroy request scope bean: {}", uuid, this);
    }
}
