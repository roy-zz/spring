package com.roy.spring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
public class DBConnectionService {

    private String host;
    private String user;
    private String password;

    public DBConnectionService() {
        log.info("[Call Constructor] url: {}, user: {}, password: {}", host, user, password);
    }

    public void init(String url, String user, String password) {
        this.host = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        log.info("[Connected] url: {}, user: {}, password: {}", host, user, password);
    }

    public void disconnect() {
        log.info("[Disconnected] url: {}", host);
    }

    public void sendQuery(String query) {
        log.info("[Send Query] url: {}, sql: {}", host, query);
    }

    @PostConstruct
    public void init() throws Exception {
        connect();
        log.info("초기화");
    }

    @PreDestroy
    public void destroy() throws Exception {
        disconnect();
        log.info("소멸");
    }

}
