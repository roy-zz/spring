package com.roy.spring.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldService {
    private String nameStore;

    public void setNameStore(String name) {
        try {
            log.info("save name = {} -> nameStore = {}", name, nameStore);
            nameStore = name;
            Thread.sleep(1000);
            log.info("find nameStore = {}", nameStore);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
