package com.roy.spring.myproxy.jdkdynamic;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ReflectionTest {

    @Test
    void notApplyReflectionTest() {
        Friend target = new Friend();

        log.info("Start");
        String result1 = target.callRoy();
        log.info("result1 = {}", result1);

        log.info("Start");
        String result2 = target.callPerry();
        log.info("result2 = {}", result2);
    }

    @Test
    void applyReflectionTest() throws Exception {
        Class<?> classFriend = Class.forName("com.roy.spring.myproxy.jdkdynamic.ReflectionTest$Friend");

        Friend target = new Friend();

        Method methodCallRoy = classFriend.getMethod("callRoy");
        Object result1 = methodCallRoy.invoke(target);
        log.info("result1 = {}", result1);

        Method methodCallPerry = classFriend.getMethod("callPerry");
        Object result2 = methodCallPerry.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Test
    void extractCommonTest() throws Exception {
        Class<?> classFriend = Class.forName("com.roy.spring.myproxy.jdkdynamic.ReflectionTest$Friend");

        Friend target = new Friend();
        Method methodCallRoy = classFriend.getMethod("callRoy");
        dynamicCallMethod(methodCallRoy, target);

        Method methodCallPerry = classFriend.getMethod("callPerry");
        dynamicCallMethod(methodCallPerry, target);
    }

    private void dynamicCallMethod(Method method, Object target) throws Exception {
        log.info("Start");
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }

    @Slf4j
    static class Friend {
        public String callRoy() {
            log.info("Call Roy");
            return "ROY";
        }
        public String callPerry() {
            log.info("Call Perry");
            return "PERRY";
        }
    }

}
