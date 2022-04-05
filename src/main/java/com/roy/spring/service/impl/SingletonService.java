package com.roy.spring.service.impl;

public class SingletonService {

    private static final SingletonService INSTANCE = new SingletonService();

    public static SingletonService getInstance() {
        return INSTANCE;
    }

    private SingletonService() {}

    public void logic() {
        System.out.println("싱글톤 로직 실행");
    }

}
