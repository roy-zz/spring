package com.roy.spring.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {
    private ThreadLocal<String>  nameStore = new ThreadLocal<>();

    public void setNameStore(String name) {
        try {
            log.info("save name = {} -> nameStore = {}", name, nameStore.get());
            nameStore.set(name);
            Thread.sleep(1000);
            log.info("find nameStore = {}", nameStore.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
