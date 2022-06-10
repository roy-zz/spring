package com.roy.spring.myproxy.proxy;

import com.roy.spring.myproxy.proxy.code.CacheProxy;
import com.roy.spring.myproxy.proxy.code.ProxyPatternClient;
import com.roy.spring.myproxy.proxy.code.RealSubject;
import com.roy.spring.myproxy.proxy.code.Subject;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void proxyTest() {
        Subject realSubject = new RealSubject();
        Subject cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }

}
