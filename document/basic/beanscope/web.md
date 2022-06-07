이번 장에서는 [빈의 프로토타입 스코프(링크)](https://imprint.tistory.com/174?category=1003393)에 이어 웹 스코프에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 웹 스코프

웹 스코프는 이름에서 알 수 있듯이 웹 환경에서만 동작한다.
웹 스코프는 프로토타입 스코프와는 다르게 해당 스코프의 종료시점까지 관리되기 때문에 소멸 메서드가 호출된다.

웹 스코프의 종류는 아래와 같다.

- **request**: HTTP요청이 들어와서 나갈 때까지 유지되는 스코프이며 요청마다 별도의 빈 객체가 생성되고 관리된다.

- **session**: HTTP의 Session과 동일한 생명주기를 갖는 스코프

- **application**: 서블릿 컨텍스트와 동일한 생명주기를 갖는 스코프

- **websocket**: 웹 소켓과 동일한 생명주기를 가지는 스코프

웹 환경을 만들기 위해서는 build.gradle에 라이브러리를 추가해야한다.

```
implementation 'org.springframework.boot:spring-boot-starter-web'
```

웹 라이브러리가 추가되면 이제 AnnotationConfigApplicationContext 대신 웹과 관련된 설정과 환경들이 추가된 
AnnotationConfigServletWebServerApplicationContext를 기반으로 어플리케이션이 구동한다.

---

### Request 스코프

로그를 출력하기 위한 MyLogger 클래스를 추가한다.

```java
@Slf4j
@Setter
@Component
@Scope(value = "request")
public class MyLogger {
    private String uuid;
    private String requestUrl;

    public void print(String message) {
        log.info("[{}] [{}] {}", uuid, requestUrl, message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        log.info("[{}] Created request scope bean: {}", uuid, this);
    }
    @PreDestroy
    public void destroy() {
        log.info("[{}] Destroy request scope bean: {}", uuid, this);
    }
}
```

@Scope 어노테이션에 request 생명주기를 지정하였다.
@PostConstruct 어노테이션으로 초기화 메서드를 지정하였으며 초기화 메서드에서 고유 아이디를 생성한다.
@PreDestroy를 사용해서 종료 메시지를 남긴다.

클라이언트로 부터 가장 앞단에서 요청을 받을 Controller를 생성한다.

```java
@RestController
@RequiredArgsConstructor
public class MyLoggerController {
    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLoggerService myLoggerService;
    @RequestMapping("/my-logger")
    public String myLogger(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestUrl(requestUrl);
        myLogger.print("call controller");
        myLoggerService.myLoggerServiceLogic("testId");
        return "OK";
    }
}
```

ObjectProvider로 부터 MyLogger 빈을 제공받는다.

로직을 처리하는 Service를 생성한다.

```java
@Service
@RequiredArgsConstructor
public class MyLoggerService {
    private final ObjectProvider<MyLogger> myLoggerProvider;
    public void myLoggerServiceLogic(String id) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.print("service id: " + id);
    }
}
```

스프링 프로젝트를 실행시키고 브라우저에서 localhost:8080/my-logger로 접속하고 프로젝트의 로그를 확인해본다.

```bash
[9b3aa615-f543-4f5a-b043-94a43af699c3] Created request scope bean: com.roy.spring.configuration.MyLogger@79a0e224
[9b3aa615-f543-4f5a-b043-94a43af699c3] [http://localhost:8080/my-logger] call controller
[9b3aa615-f543-4f5a-b043-94a43af699c3] [http://localhost:8080/my-logger] service id: testId
[9b3aa615-f543-4f5a-b043-94a43af699c3] Destroy request scope bean: com.roy.spring.configuration.MyLogger@79a0e224
[166ea9fb-d61f-48e1-908b-962daf42a2dd] Created request scope bean: com.roy.spring.configuration.MyLogger@79f8f40c
[166ea9fb-d61f-48e1-908b-962daf42a2dd] [http://localhost:8080/my-logger] call controller
[166ea9fb-d61f-48e1-908b-962daf42a2dd] [http://localhost:8080/my-logger] service id: testId
[166ea9fb-d61f-48e1-908b-962daf42a2dd] Destroy request scope bean: com.roy.spring.configuration.MyLogger@79f8f40c
```

접속해서 응답할 때까지 고유한 uuid가 유지되는 것을 확인할 수 있다.

---

### 스코프와 프록시

우리는 지금까지 SingletonBean과 PrototypeBean을 동시에 사용하기 위해 ObjectProvider를 사용했다.
하지만 매번 사용할 때마다 새로 꺼내는 코드를 작성해야하고 어지간히 귀찮지 않을 수 없다.

싱글톤 빈처럼 간편하게 사용하는 방법이 있다.
@Scope 어노테이션에 proxyMode = ScopedProxyMode.TARGET_CLASS 속성을 추가해준다.

```java
@Slf4j
@Setter
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    // 생략
}
```

빈을 사용하는 코드에 더 이상 ObjectProvider를 사용하지 않고 싱글톤 빈과 같은 방식으로 수정해본다.

```java
@RestController
@RequiredArgsConstructor
public class MyLoggerController {

    private final MyLogger myLogger;
    private final MyLoggerService myLoggerService;

    @RequestMapping("/my-logger")
    public String myLogger(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        myLogger.setRequestUrl(requestUrl);
        myLogger.print("call controller");
        myLoggerService.myLoggerServiceLogic("testId");
        return "OK";
    }

}
```

```java
@Service
@RequiredArgsConstructor
public class MyLoggerService {
    private final MyLogger myLogger;
    public void myLoggerServiceLogic(String id) {
        myLogger.print("service id: " + id);
    }
}
```

프로젝트를 다시 실행시키고 동일한 주소로 접속하면 수정 전과 동일한 결과를 얻을 수 있다.
디버거를 사용하거나 로그를 찍어서 MyLogger의 정체를 확인해본다.

```bash
myLogger = class com.roy.spring.configuration.MyLogger$$EnhancerBySpringCGLIB$$3a8e6bb4
```

이번에도 스프링이 CGLIB를 사용하여 프록시 객체를 주입시켰다.
결국 스프링이 빈 프록시 객체가 자리하고 있다가 MyLogger를 사용하는 시점에 새로운 객체를 생성하여 사용하게 해주는 것이다.
겉모습만 보면 싱글톤 스코프와 동일하게 작동하는 것 같지만 내부적으로는 다르게 작동하기 때문에 주의해서 사용해야한다.

---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/