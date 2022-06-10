package com.roy.spring.myproxy.application.version1;

public class OrderRepositoryVersion1Impl implements OrderRepositoryVersion1 {

    @Override
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
