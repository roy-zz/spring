package com.roy.spring.service;

import com.roy.spring.configuration.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyLoggerService {
    private final MyLogger myLogger;
    public void myLoggerServiceLogic(String id) {
        myLogger.print("service id: " + id);
    }
}
