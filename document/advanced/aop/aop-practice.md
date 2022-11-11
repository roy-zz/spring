[이전 장(링크)](https://imprint.tistory.com/360)까지 `스프링 AOP 포인트컷의 지시자`에 대해서 알아보았다.  
이번 장에서는 실제 실무에서 사용될만한 예제를 만들어보는 시간을 가져본다.  
모든 코드는 [깃허브(링크)](https://github.com/roy-zz/spring) 에 올려두었다.
---

## 예제 코드

이번 장에서 우리의 목표는 아래와 같은 애너테이션을 생성하여 프로젝트에 적용시키는 것이다.
- `@Trace`: 애너테이션으로 로그를 출력
- `@Retry`: 애너테이션으로 예외 발생시 재시도
  
AOP를 적용하기 위해 간단히 `Service`, `Repositry` 클래스를 작성해본다.

**ExamRepository**
```java
@Repository
public class ExamRepository {

    private static int seq = 0;

    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}
```

5번 시도하면 1번은 실패하도록 리포지토리를 생성하였다. 추후 애너테이션이 추가되면 실패할 때 자동으로 재시도하도록 유도한다.

**ExamService**
```java
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public void request(String itemId) {
        examRepository.save(itemId);
    }
}
```

**ExamTest**
```java
@Slf4j
@SpringBootTest
public class ExamTest {

    @Autowired
    private ExamService examService;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            examService.request("data: " + i);
        }
    }
}
```

테스트 코드를 실행해보면 우리의 예상처럼 5번째 요청에 실패하는 것을 확인할 수 있다.

---

## 로그 출력 AOP

로그 출력용 AOP를 생성해본다.  
우리가 만들게 될 `@Trace`가 메서드에 붙어 있으면 호출 정보가 출력되는 편리한 기능이다.

**@Trace**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}
```

**TraceAspect**
```java
@Slf4j
@Aspect
public class TraceAspect {

    @Before("@annotation(com.roy.spring.myaop.example.annotation.Trace)")
    public void doTrace(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[trace] {} args = {}", joinPoint.getSignature(), args);
    }
}
```

`@annotation(com.roy.spring.myaop.example.annotation.Trace)` 포인트컷을 사용해서 `@Trace`가 붙은 메서드에 어드바이스를 적용한다.

**ExamService - @Trace 추가**

```java
@Service
@RequiredArgsConstructor
public class ExamService {
    // ...
    @Trace
    public void request(String itemId) {
        examRepository.save(itemId);
    }
}
```

`request()` 메서드에 `@Trace` 에너테이션을 추가하여 메서드 호출 정보를 AOP를 사용해서 로그로 남기도록 하였다.

**ExamRepository - @Trace 추가**

```java
@Repository
public class ExamRepository {
    // ...
    @Trace
    public String save(String itemId) {
        // ...
    }
}
```

`save()` 메서드에 `@Trace` 애너테이션을 추가하여 로그가 출력되도록 하였다.

**ExamTest - TraceAspect 빈으로 추가**
```java
@Slf4j
@SpringBootTest
@Import({TraceAspect.class})
public class ExamTest {
    // ...
}

```

`@Import(TraceAspect.class)`를 추가하여 `TraceAspect`를 스프링 빈으로 추가하도록 하였다.  
테스트 코드를 실행시켜보면 아래와 같이 애너테이션이 추가되면서 AOP가 적용되는 것을 확인할 수 있다.

```shell
client request i = 0
[trace] void com.roy.spring.myaop.example.ExamService.request(String) args = [data: 0]
[trace] String com.roy.spring.myaop.example.ExamRepository.save(String) args = [data: 0]
...
client request i = 4
[trace] void com.roy.spring.myaop.example.ExamService.request(String) args = [data: 4]
[trace] String com.roy.spring.myaop.example.ExamRepository.save(String) args = [data: 4]
```

---

## 재시도 AOP

이번에는 `@Retry`애너테이션을 만들어 예외가 발생했을 때 재시도하도록 해본다.

**Retry**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int value() default 3;
}
```

재시도 횟수로 사용할 값을 매개변수로 받을 수 있도록 한다.

**RetryAspect**
```java
@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} retry = {}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
            try {
                log.info("[retry] try count = {} / {}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception exception) {
                exceptionHolder = exception;
            }
        }
        throw exceptionHolder;
    }
}
```

- 재시도를 위한 애스펙트다.
- `@annotation(retry)`, `Retry retry`를 사용해서 어드바이스에 애너테이션을 파라미터로 전달한다.
- `retry.value()`를 통해서 애너테이션에 지정한 값을 가져올 수 있다.
- 예외가 발생해서 결과가 정상 반환되지 않으면 `retry.value()`만큼 재시도한다.

**ExamRepository - `@Retry`추가**
```java
@Repository
public class ExamRepository {
    // ...
    @Trace
    @Retry(value = 4)
    public String save(String itemId) {
        // ...
    }
}
```

- `ExamRepository.save()` 메소드에 `@Retry(value = 4)`를 적용했다. 예외가 발생하는 경우 4번의 재시도를 한다.

**ExamTest**
```java
@Slf4j
@SpringBootTest
@Import({TraceAspect.class, RetryAspect.class})
public class ExamTest {
    // ...
}
```

- `@Import({TraceAspect.class, RetryAspect.class})`를 통해 `RetryAspect`가 스프링 빈으로 등록되도록 하였다.  
테스트 코드를 실행하면 출력되는 결과는 아래와 같다.

```shell
[retry] String com.roy.spring.myaop.example.ExamRepository.save(String) retry = @com.roy.spring.myaop.example.annotation.Retry(value=4)
[retry] try count = 1 / 4
[retry] String com.roy.spring.myaop.example.ExamRepository.save(String) retry = @com.roy.spring.myaop.example.annotation.Retry(value=4)
[retry] try count = 1 / 4
[retry] String com.roy.spring.myaop.example.ExamRepository.save(String) retry = @com.roy.spring.myaop.example.annotation.Retry(value=4)
[retry] try count = 1 / 4
[retry] String com.roy.spring.myaop.example.ExamRepository.save(String) retry = @com.roy.spring.myaop.example.annotation.Retry(value=4)
[retry] try count = 1 / 4
[retry] String com.roy.spring.myaop.example.ExamRepository.save(String) retry = @com.roy.spring.myaop.example.annotation.Retry(value=4)
[retry] try count = 1 / 4
[retry] try count = 2 / 4
```

마지막 출력을 확인해보면 1번 실패하고 재시도된 것을 확인할 수 있다.  
스프링에서 제공하는 `@Transactional`이 가장 대표적인 AOP이다.

---

**참고한 자료**:

- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2