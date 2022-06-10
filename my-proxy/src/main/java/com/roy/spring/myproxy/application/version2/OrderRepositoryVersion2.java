package com.roy.spring.myproxy.application.version2;

public class OrderRepositoryVersion2 {

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
