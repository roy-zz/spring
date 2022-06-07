package com.roy.spring.basic.controller;

import com.roy.spring.basic.configuration.MyLogger;
import com.roy.spring.basic.service.MyLoggerService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
