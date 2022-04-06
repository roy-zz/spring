package com.roy.spring.controller;

import com.roy.spring.configuration.MyLogger;
import com.roy.spring.service.MyLoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class MyLoggerController {

    private final MyLogger myLogger;
    private final MyLoggerService myLoggerService;

    @RequestMapping("/my-logger")
    public String myLogger(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        System.out.println("myLogger = " + myLogger.getClass());
        myLogger.setRequestUrl(requestUrl);
        myLogger.print("call controller");
        myLoggerService.myLoggerServiceLogic("testId");
        return "OK";
    }

}
