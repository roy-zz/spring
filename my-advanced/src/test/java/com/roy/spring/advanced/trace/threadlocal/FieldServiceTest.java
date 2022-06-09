package com.roy.spring.advanced.trace.threadlocal;

import com.roy.spring.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private final FieldService fieldService = new FieldService();

    @Test
    void fieldServiceTest() throws InterruptedException {
        log.info("main thread start");
        Thread threadRoy = new Thread(() -> fieldService.setNameStore("Roy"), "Thread-Roy");
        Thread threadPerry = new Thread(() -> fieldService.setNameStore("Perry"), "Thread-Perry");

        threadRoy.start();
        Thread.sleep(100);
        threadPerry.start();

        Thread.sleep(3000);
        log.info("main thread destroy");
    }

}
