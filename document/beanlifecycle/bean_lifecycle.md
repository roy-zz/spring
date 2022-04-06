이번 장에서는 빈의 생명주기와 콜백에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 빈의 생명주기와 콜백

어플리케이션은 시작 시점과 종료 시점에 진행해야하는 작업들이 있다.
스프링 빈 또한 이러한 초기화 작업과 종료 작업이 필요하며 어떻게 사용하는지에 대해서 알아보도록 한다.

테스를 위해 데이터베이스 커넥션을 담당하는 DBConnectionService를 생성하였다.

```java
@Slf4j
public class DBConnectionService {

    private String host;
    private String user;
    private String password;

    public DBConnectionService() {
        log.info("url: {}, user: {}, password: {}", host, user, password);
        connect();
        sendQuery("SELECT 1");
    }

    public void init(String url, String user, String password) {
        this.host = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        log.info("[Connected] url: {}, user: {}, password: {}", host, user, password);
    }

    public void disconnect() {
        log.info("[Disconnected] url: {}", host);
    }

    public void sendQuery(String query) {
        log.info("[Send Query] url: {}, sql: {}", host, query);
    }
}
```

테스트 코드를 사용하여 빈을 주입받아서 사용해본다.

```java
class BeanLifecycleTest {
    @Test
    @DisplayName("빈 생명주기 테스트")
    void beanLifecycleTest() {
        ConfigurableApplicationContext cac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        DBConnectionService service = cac.getBean(DBConnectionService.class);
        cac.close();
    }

    @Configuration
    static class LifecycleConfig {
        @Bean
        public DBConnectionService dbConnectionService() {
            DBConnectionService dbConnectionService = new DBConnectionService();
            dbConnectionService.init("jdbc://com.roy.spring", "royId", "royPw");
            return dbConnectionService;
        }
    }
}
```

출력된 결과는 아래와 같다.

```bash
url: null, user: null, password: null
[Connected] url: null, user: null, password: null
[Send Query] url: null, sql: SELECT 1
```

생성자를 사용하여 빈을 생성한 이후에 접속 정보(url, id, password)입력없이 접속을 요청하였기 때문에 최초 연결시점에 데이터가 없는 것은 당연하다.

객체 생성이 단순히 내부값을 채우는 정도로 간단하지 않다면 생성과 초기화를 분리해야한다.
필자가 작성한 DBConnectionService를 예로 들면 url, id, password를 입력받아서 메모리를 할당하는 작업은 간단하지만 DB에 Connect하는 작업은 상대적으로 무겁다.
이러한 작업들이 객체를 생성하는 생성자안에서 한 번에 일어나는 것보다 메모리를 할당하는 작업과 초기화 작업을 나누어 진행하는 것이 유지보수 관점에서 좋다.

간단하게 스프링 빈은 **객체 생성 -> 의존관계 주입** 의 생명주기를 가지며 이것보다 더 자세한 라이프사이클은 아래와 같다.
스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백 -> 스프링 종료
**초기화 콜백**은 빈이 생성되고 빈의 의존관계 주입이 완료된 후 호출되며, **소멸전 콜백**은 빈이 소멸되기 직전에 호출된다.

스프링은 빈 생명주기 콜백을 크게 세가지 방법으로 지원한다.
- 인터페이스(InitializingBean, DisposableBean) 사용
- 설정 정보에 초기화 메서드, 종료 메서드 지정
- @PostConstruct, @PreDestroy 어노테이션 사용

---

### 인터페이스 (InitializingBean, DisposableBean) 사용

InitializingBean, DisposableBean을 implements하고 초기화 시 호출되는 afterPropertiesSet와 소멸 시 호출되는 destroy를 구현한다.

```java
@Slf4j
public class DBConnectionService implements InitializingBean, DisposableBean {
    private String host;
    private String user;
    private String password;
    public DBConnectionService() {
        log.info("url: {}, user: {}, password: {}", host, user, password);
        connect();
        sendQuery("SELECT 1");
    }

    public void init(String url, String user, String password) {
        this.host = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        log.info("[Connected] url: {}, user: {}, password: {}", host, user, password);
    }

    public void disconnect() {
        log.info("[Disconnected] url: {}", host);
    }

    public void sendQuery(String query) {
        log.info("[Send Query] url: {}, sql: {}", host, query);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        log.info("초기화 성공");
    }

    @Override
    public void destroy() throws Exception {
        disconnect();
    }
}
```

동일한 테스트를 진행하고 결과를 확인해보면 아래와 같다.

```bash
[Call Constructor] url: null, user: null, password: null
[Connected] url: null, user: null, password: null
[Send Query] url: null, sql: SELECT 1
[Connected] url: jdbc://com.roy.spring, user: royId, password: royPw
초기화 성공
Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@942a29c
[Disconnected] url: jdbc://com.roy.spring
```

객체 생성 이후에 초기화가 진행되면서 필요한 정보가 입력된 것을 확인할 수 있다.
또한 ApplicationContext가 종료되는 시점에 소멸 콜백이 호출되었다.

하지만 우리의 서비스 코드가 스프링 전용 인터페이스에 의존한다는 단점이 있다.
이미 jar로 빌드되어있는 외부 라이브러리를 사용할 때 적용할 수 없다.
스프링 초창기에 개발된 기능이며 현재는 거의 사용되지 않는다.

---

### @Bean의 속성값을 사용한 방법

@Bean 어노테이션에 initMethod, destroyMethod처럼 초기화 소멸 메서드를 지정해서 사용할 수 있다.

변경된 코드는 아래와 같다.
init 메서드와 destroy 메서드가 추가되었다.

```java
@Slf4j
public class DBConnectionService {
    // 생략
    public void init() throws Exception {
        connect();
        log.info("초기화");
    }

    public void destroy() throws Exception {
        disconnect();
        log.info("소멸");
    }
}
```

@Bean을 사용할 때 initMethod로 init를 지정하였고 destroyMethod로 destroy를 지정하였다.

```java
class BeanLifecycleTest {
    @Test
    @DisplayName("빈 생명주기 테스트")
    void beanLifecycleTest() {
        ConfigurableApplicationContext cac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        DBConnectionService service = cac.getBean(DBConnectionService.class);
        cac.close();
    }

    @Configuration
    static class LifecycleConfig {
        @Bean(initMethod = "init", destroyMethod = "destroy")
        public DBConnectionService dbConnectionService() {
            DBConnectionService dbConnectionService = new DBConnectionService();
            dbConnectionService.init("jdbc://com.roy.spring", "royId", "royPw");
            return dbConnectionService;
        }
    }
}
```

출력 결과는 아래와 같다.

```bash
[Call Constructor] url: null, user: null, password: null
[Connected] url: jdbc://com.roy.spring, user: royId, password: royPw
초기화
Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7db12bb6, started on Wed Apr 06 16:14:17 KST 2022
[Disconnected] url: jdbc://com.roy.spring
소멸
```

우리가 원하는 결과가 출력된 것을 확인할 수 있다.
우리가 작성한 클래스가 스프링 코드에 의존하지 않으며 인터페이스를 구현하여 사용하는 것과는 다르게 메서드 이름을 자유롭게 지정할 수 있다.
코드 레벨에서 지정하는 것이 아니라 어노테이션의 속성으로 지정하는 것이기 때문에 외부 라이브러리에도 초기화, 종료 메서드를 지정할 수 있다.

@Bean의 destroyMethod를 확인해보면 기본값이 지정되어 있는 것을 확인할 수 있다.

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    // 생략
    /**
     * The optional name of a method to call on the bean instance upon closing the
     * application context, for example a {@code close()} method on a JDBC
     * {@code DataSource} implementation, or a Hibernate {@code SessionFactory} object.
     * The method must have no arguments but may throw any exception.
     * <p>As a convenience to the user, the container will attempt to infer a destroy
     * method against an object returned from the {@code @Bean} method. For example, given
     * an {@code @Bean} method returning an Apache Commons DBCP {@code BasicDataSource},
     * the container will notice the {@code close()} method available on that object and
     * automatically register it as the {@code destroyMethod}. This 'destroy method
     * inference' is currently limited to detecting only public, no-arg methods named
     * 'close' or 'shutdown'. The method may be declared at any level of the inheritance
     * hierarchy and will be detected regardless of the return type of the {@code @Bean}
     * method (i.e., detection occurs reflectively against the bean instance itself at
     * creation time).
     * <p>To disable destroy method inference for a particular {@code @Bean}, specify an
     * empty string as the value, e.g. {@code @Bean(destroyMethod="")}. Note that the
     * {@link org.springframework.beans.factory.DisposableBean} callback interface will
     * nevertheless get detected and the corresponding destroy method invoked: In other
     * words, {@code destroyMethod=""} only affects custom close/shutdown methods and
     * {@link java.io.Closeable}/{@link java.lang.AutoCloseable} declared close methods.
     * <p>Note: Only invoked on beans whose lifecycle is under the full control of the
     * factory, which is always the case for singletons but not guaranteed for any
     * other scope.
     * @see org.springframework.beans.factory.DisposableBean
     * @see org.springframework.context.ConfigurableApplicationContext#close()
     */
    String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;
}
```

내용을 간략하게 살펴보면 @Bean은 반환된 객체에 대해서 파괴 방법을 유추하려하고 있다.
Argument가 없으며 이름이 close, shutdown인 메서드를 호출한다고 나와있다.
만약 이러한 기본 소멸 전략을 무시하고 싶다면 destroyName = ""과 같이 지정해주어야 한다.

---

### @PostConstruct, @PreDestroy

비교적 최근에 나온 방식이며 원하는 메서드에 어노테이션을 지정하여 사용하는 방법이다.
수정된 DBConnectionService는 아래와 같다.

```java
@Slf4j
public class DBConnectionService {
    // 생략
    @PostConstruct
    public void init() throws Exception {
        connect();
        log.info("초기화");
    }

    @PreDestroy
    public void destroy() throws Exception {
        disconnect();
        log.info("소멸");
    }
}
```

빈을 사용하는 쪽은 수정할 필요없이 @Bean 어노테이션만 사용하면 된다.

```java
class BeanLifecycleTest {
    @Test
    @DisplayName("빈 생명주기 테스트")
    void beanLifecycleTest() {
        ConfigurableApplicationContext cac = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        DBConnectionService service = cac.getBean(DBConnectionService.class);
        cac.close();
    }

    @Configuration
    static class LifecycleConfig {
        @Bean
        public DBConnectionService dbConnectionService() {
            DBConnectionService dbConnectionService = new DBConnectionService();
            dbConnectionService.init("jdbc://com.roy.spring", "royId", "royPw");
            return dbConnectionService;
        }
    }
}
```

출력 결과는 우리가 예상한 것과 동일하다.

```bash
[Call Constructor] url: null, user: null, password: null
[Connected] url: jdbc://com.roy.spring, user: royId, password: royPw
초기화
Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@942a29c
[Disconnected] url: jdbc://com.roy.spring
소멸
```

스프링에서 권장하는 방식이며 어노테이션만 붙이면 되기 때문에 가장 간단한 방식이다.
또한 어노테이션이 javax에 포함된 자바 표준 기술이다.

---

지금까지 빈의 생명주기와 콜백에 대해서 알아보았다.
정리하면 대부분의 경우에 @PostConstructor, @PreDestroy를 사용하고 외부 라이브러리를 빈으로 등록하는 경우에는 @Bean의 initMethod, destroyMethod 속성을 사용하자.

---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/