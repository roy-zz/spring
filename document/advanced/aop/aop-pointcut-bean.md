[이전 장(링크)]() 에서는 `within, args, @target, @within, @annotation, @args 지시자`에 대해서 알아보았다.  
이번 장에서는 **bean 지시자**에 대해서 알아보도록 한다.  
모든 코드는 [깃허브(링크)](https://github.com/roy-zz/spring) 에 올려두었다.
---

## bean

- 스프링 전용 포인트컷 지시자로 빈의 이름으로 지정한다.  
- 스프링 빈의 이름으로 AOP 적용 여부를 지정한다. 스프링에서만 사용할 수 있는 특별한 지시자다.
- `bean(orderService) || bean(*Repository)`와 같은 문법으로 사용된다.
- `*`와 같은 패턴을 사용할 수 있다.

**BeanTest**

```java
@Slf4j
@SpringBootTest
@Import(BeanTest.BeanAspect.class)
public class BeanTest {

    @Autowired
    private OrderService orderService;

    @Test
    void success() {
        orderService.orderItem("itemRoy");
    }

    @Aspect
    static class BeanAspect {

        @Around("bean(orderService) || bean(*Repository)")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[bean] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
```

`OrderService`, `*Repository(OrderRepository)`의 메서드에 AOP가 적용된다.  
출력된 결과는 아래와 같다.

```shell
[bean] void com.roy.spring.myaop.order.OrderService.orderItem(String)
[orderService] 실행
[bean] String com.roy.spring.myaop.order.OrderRepository.save(String)
[orderRepository] 실행
```

---

## 매개변수 전달

아래의 포인트컷 표현식을 사용하면 어드바이스에 매개변수를 전달할 수 있다.
`this`, `target`, `args`, `@target`, `@within`, `@annotation`, `@args`  
  
사용법은 아래와 같다.
```java
@Before("allMember() && args(arg,..")
public void logArgs3(Strign arg) {
    log.info("[logArgs3] arg = {}", arg);
}
```

- 포인트컷의 이름과 매개변수의 이름을 맞추어야 한다. 여기서는 `arg`로 맞춰져있다.
- 추가로 타입이 메서드에 지정한 타입으로 제한된다. 여기에서 메서드의 타입이 `String`으로 되어 있기 때문에 아래와 같이 정의된 것으로 이해하면 된다.
  - `args(arg,..)` -> `args(String,..)`

**ParameterTest**
```java
@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {

    @Autowired
    private MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloRoy");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* com.roy.spring.myaop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1] {}, arg = {}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2] {}, arg = {}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg = {}", arg);
        }

        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {}, obj = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, obj = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation] {}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }
    }
}
```

- `logArgs1`: `joinPoint.getArgs()[0]`와 같이 매개변수를 전달 받는다.
- `logArgs2`: `args(arg,..)`와 같이 매개변수를 전달 받는다.
- `logArgs3`: `@Before`를 사용한 축약 버전이다. 추가로 타입을 `String`으로 제한했다.
- `this`: 프록시 객체를 전달 받는다.
- `target`: 실제 대상 객체를 전달 받는다.
- `@target`, `@within`: 타입의 애너테이션을 전달 받는다.
- `@annotation`: 메소드의 애너테이션을 전달받는다. 여기서는 `annotation.value()`로 해당 애너테이션의 값을 출력하였다.
  
출력된 결과는 아래와 같다.

```shell
memberService Proxy = class com.roy.spring.myaop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$6dcb441c
[logArgs1] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), arg = helloRoy
[logArgs2] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), arg = helloRoy
[logArgs3] arg = helloRoy
[this] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), obj = class com.roy.spring.myaop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$6dcb441c
[target] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), obj = class com.roy.spring.myaop.member.MemberServiceImpl
[@target] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), obj = @com.roy.spring.myaop.member.annotation.ClassAop()
[@within] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), obj = @com.roy.spring.myaop.member.annotation.ClassAop()
[@annotation] String com.roy.spring.myaop.member.MemberServiceImpl.hello(String), annotationValue = test value
```

---

## this, target




---

**참고한 자료**:

- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2