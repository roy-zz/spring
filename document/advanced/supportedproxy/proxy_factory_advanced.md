[이전 장(링크)](https://imprint.tistory.com/349) 에서는 포인트컷(Pointcut)에 대해서 알아보았다.
이번 장에서는 **프록시 팩토리**를 조금 더 깊게 알아보도록 한다.
모든 코드는 [깃허브(링크)](https://github.com/roy-zz/spring) 에 올려두었다.
---

### 프록시 팩토리 적용 및 활용

#### Version 1

프록시 팩토리를 사용해서 애플리케이션에 프록시를 만들어보도록 한다.  
인터페이스가 있는 `version 1` 애플리케이션에 `LogTrace` 기능을 프록시 팩토리를 통해서 프록시를 만들어 적용해본다.

아래는 사용될 어드바이스 코드이다.

**LogTraceAdvice**
```java
public class LogTraceAdvice implements MethodInterceptor {

    private final LogTrace logTrace;

    public LogTraceAdvice(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TraceStatus status = null;
        try {
            Method method = invocation.getMethod();
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            Object result = invocation.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception exception) {
            logTrace.exception(status, exception);
            throw exception;
        }
    }
}
```

**ProxyFactoryConfigVersion1**
```java
@Slf4j
@Configuration
public class ProxyFactoryConfigVersion1 {

    @Bean
    public OrderControllerVersion1 orderControllerVersion1(LogTrace logTrace) {

        OrderControllerVersion1 orderController = new OrderControllerVersion1Impl(orderServiceVersion1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerVersion1 proxy = (OrderControllerVersion1) factory.getProxy();
        log.info("ProxyFactory Controller proxy = {}, target = {}", proxy.getClass(), orderController.getClass());

        return proxy;
    }
    
    @Bean
    public OrderServiceVersion1 orderServiceVersion1(LogTrace logTrace) {

        OrderServiceVersion1 orderService = new OrderServiceVersion1Impl(orderRepositoryVersion1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceVersion1 proxy = (OrderServiceVersion1) factory.getProxy();
        log.info("ProxyFactory Service proxy = {}, target = {}", proxy.getClass(), orderService.getClass());

        return proxy;
    }

    @Bean
    public OrderRepositoryVersion1 orderRepositoryVersion1(LogTrace logTrace) {

        OrderRepositoryVersion1Impl orderRepository = new OrderRepositoryVersion1Impl();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryVersion1 proxy = (OrderRepositoryVersion1) factory.getProxy();
        log.info("ProxyFactory Repository proxy = {}, target = {}", proxy.getClass(), orderRepository.getClass());

        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
```

- 포인트컷은 `NameMatchMethodPointcut`을 사용한다. 여기에는 심플 매칭 기능이 있어서 애스터리스크(`*`)를 매칭할 수 있다.
  - `request*`, `order*`, `save*`: `request`로 시직하는 메서드에 포인트컷은 `true`를 반환하며 나머지도 동일하다.
  - 이렇게 설정한 이유는 `noLog()` 메서드에는 어드바이스를 적용하지 않기 위해서다.
- 어드바이저는 포인트컷(`NameMatchMethodPointcut`), 어드바이스(`LogTraceAdvice`)를 가지고 있다.
- 프록시 팩토리에 각각의 `target`과 `advisor`를 등록해서 프록시를 생성하고, 이렇게 생성된 프록시를 스프링 빈으로 등록한다.

메인 메서드에서 위에서 작성한 `Bean`을 등록하도록 코드를 수정한다.
**MyProxyApplication**
```java
@Import(ProxyFactoryConfigVersion1.class)
@SpringBootApplication(scanBasePackages = "com.roy.spring.myproxy.application")
public class MyProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProxyApplication.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
```

메인 메서드를 실행하고 출력된 결과는 아래와 같다.

```shell
ProxyFactory Repository proxy = class com.sun.proxy.$Proxy52, 
target = class com.roy.spring.myproxy.application.version1.OrderRepositoryVersion1Impl
ProxyFactory Service proxy = class com.sun.proxy.$Proxy54, 
target = class com.roy.spring.myproxy.application.version1.OrderServiceVersion1Impl
ProxyFactory Controller proxy = class com.sun.proxy.$Proxy55, 
target = class com.roy.spring.myproxy.application.version1.OrderControllerVersion1Impl
```

`Version1` 애플리케이션의 경우 인터페이스가 있기 때문에 JDK 동적 프록시가 적용된 것을 확인할 수 있다.

---

#### Version 2

인터페이스가 없고 구체 클래스만 있는 `Version2` 애플리케이션에 `LogTrace` 기능을 프록시 팩토리를 통해서 프록시를 만들어 적용해본다.

**ProxyFactoryConfigVersion2**
```java
@Slf4j
@Configuration
public class ProxyFactoryConfigVersion2 {

    @Bean
    public OrderControllerVersion2 orderControllerVersion2(LogTrace logTrace) {

        OrderControllerVersion2 orderController = new OrderControllerVersion2(orderServiceVersion2(logTrace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerVersion2 proxy = (OrderControllerVersion2) factory.getProxy();
        log.info("ProxyFactory Controller proxy = {}, target = {}", proxy.getClass(), orderController.getClass());

        return proxy;
    }

    @Bean
    public OrderServiceVersion2 orderServiceVersion2(LogTrace logTrace) {

        OrderServiceVersion2 orderService = new OrderServiceVersion2(orderRepositoryVersion2(logTrace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceVersion2 proxy = (OrderServiceVersion2) factory.getProxy();
        log.info("ProxyFactory Service proxy = {}, target = {}", proxy.getClass(), orderService.getClass());

        return proxy;
    }

    @Bean
    public OrderRepositoryVersion2 orderRepositoryVersion2(LogTrace logTrace) {

        OrderRepositoryVersion2 orderRepository = new OrderRepositoryVersion2();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryVersion2 proxy = (OrderRepositoryVersion2) factory.getProxy();
        log.info("ProxyFactory Repository proxy = {}, target = {}", proxy.getClass(), orderRepository.getClass());

        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
```

메인 메서드가 있는 클래스를 수정하여 위에서 작성한 `ProxyFactoryConfigVersion2`가 스프링 빈으로 등록되도록 한다.

```java
@Import(ProxyFactoryConfigVersion2.class)
@SpringBootApplication(scanBasePackages = "com.roy.spring.myproxy.application")
public class MyProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProxyApplication.class, args);
    }

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }
}
```

메인 메서드를 실행시켜서 출력된 결과는 아래와 같다.

```shell
ProxyFactory Repository proxy = class com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2$$EnhancerBySpringCGLIB$$8026dc87, 
target = class com.roy.spring.myproxy.application.version2.OrderRepositoryVersion2
ProxyFactory Service proxy = class com.roy.spring.myproxy.application.version2.OrderServiceVersion2$$EnhancerBySpringCGLIB$$c4a20044, 
target = class com.roy.spring.myproxy.application.version2.OrderServiceVersion2
ProxyFactory Controller proxy = class com.roy.spring.myproxy.application.version2.OrderControllerVersion2$$EnhancerBySpringCGLIB$$6967a2bd, 
target = class com.roy.spring.myproxy.application.version2.OrderControllerVersion2
```

`Version 2` 애플리케이션은 인터페이스가 없고 구체 클래스만 있기 때문에 프록시 팩토리가 CGLIB를 적용한 것을 확인할 수 있다.

---

#### 정리

프록시 팩토리를 사용하면 개발자는 편리하게 프록시를 생성할 수 있다.  
추가로 어드바이저, 어드바이스, 포인트컷을 사용하면 **어떠한 기능**을 **어디**에 적용할지 결정할 수 있다.  
이러한 기능들을 통해서 원본 코드를 손대지 않고 프록시를 통해 부가 기능을 적용할 수 있지만 아직 해결되지 않은 몇 가지 문제가 있다.

**너무 많은 설정**  
위에서 생성한 `ProxyFactoryConfigVersion1`, `ProxyFactoryConfigVersion2`와 같은 설정 파일이 지나치게 많다.  
예를 들어, 애플리케이션에 스프링 빈이 100개가 있고, 프록시를 통해 부가 기능을 적용하려면 100개의 동적 프록시 생성 코드를 만들어야 한다.  
최근에는 스프링 빈을 만드는 과정도 컴포넌트 스캔을 통해서 생성한다. 하지만 직접 등록하는 것 뿐만 아니라, 프록시를 적용하는 코드까지 빈 생성 코드에 넣어야 한다.

**컴포넌트 스캔**  
`Version 3` 애플리케이션처럼 컴포넌트 스캔을 사용하는 경우 위에서 사용한 방법으로는 프록시 적용이 불가능하다.  
실제 객체를 컴포넌트 스캔으로 스프링 컨테이너에 빈으로 등록을 해버렸기 때문이다.  
우리가 지금까지 사용한 방식은 실제 객체를 컨테이너에 등록하는 것이 아니라 `ProxyFactoryConfigVersion1`처럼 부가 기능이 있는 프록시 객체를 실제 객체 대신 컨테이너에 빈으로 등록해야 한다.
  
이러한 문제를 한 번에 해결하는 방법이 **빈 후처리기**이다.

---

**참고한 자료**:

- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2