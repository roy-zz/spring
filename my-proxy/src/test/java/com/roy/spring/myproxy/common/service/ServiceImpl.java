package com.roy.spring.myproxy.common.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceImpl implements ServiceInterface {

    @Override
    public void save() {
        log.info("Call save()");
    }

    @Override
    public void find() {
        log.info("Call find()");
    }

}
