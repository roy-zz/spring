package com.roy.spring.advanced.application.version0;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryVersion0 {

    public void save(String itemId) {
        if (itemId.equals("exception")) {
            throw new IllegalStateException("예외 발생");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

}
