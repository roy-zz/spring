[이전 장(링크)]() 에서는 `스프링 AOP를 적용하는 방법`에 대해서 알아보았다.  
이번 장에서는 **스프링 AOP의 포인트컷**에 대해서 알아보도록 한다.  
모든 코드는 [깃허브(링크)](https://github.com/roy-zz/spring) 에 올려두었다.
---

## 포인트컷 지시자

"포인트컷 표현식"을 포함한 "포인트컷"에 대해서 자세하게 알아본다.
  
AspectJ는 포인트컷을 편리하게 표현하기 위한 특별한 표현식을 제공한다. (예. `@Pointcut("execution(* com.roy.spring.aop.order..*(..))")`)  
포인트컷 표현식은 "AspectJ pointcut expression"으로 AspectJ가 제공하는 포인트컷 표현식을 줄인 것을 의미하는 것이다.

### 포인트컷 지시자

포인트컷 표현식은 `execution`같은 포인트컷 지시자(Pointcut Designator)로 시작하고 줄여서 "PCD"라 한다.

#### 포인트컷 지시자의 종류

- `execution`: 메소드 실행 조인 포인트를 매칭한다. 스프링 AOP에서 가장 많이 사용하고, 기능도 복잡하다.
- `within`: 특정 타입 내의 조인 포인트를 매칭한다.
- `args`: 인자가 주어진 타입의 인스턴스인 조인 포인트
- `this`: 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
- `target`: Target 객체(스프링 AOP 프록시가 가르키는 실제 대상)를 대상으로 하는 조인 포인트
- `@target`: 실행 객체의 클래스에 주어진 타입의 애너테이션이 있는 조인 포인트
- `@within`: 주어진 애너테이션이 있는 타입 내 조인 포인트
- `@annotation`: 메서드가 주어진 애너테이션을 가지고 있는 조인 포인트를 매칭
- `@args`: 전달된 실제 인수의 런타임 타입이 주어진 타입의 애너테이션을 갖는 조인 포인트
- `bean`: 스프링 전용 포인트컷 지시자로, 빈의 이름으로 포인트컷을 지정한다.
  
사실 `execution`을 제외한 지시자는 많이 사용되지 않는다.

---

## 포인트컷 예제 생성

포인트컷을 실습하기 위해 예제 파일을 생성한다.

**ClassAop**
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {
}
```

**MethodAop**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAop {
    String value();
}
```

**MemberService**
```java
public interface MemberService {
    String hello(String param);
}
```

**MemberServiceImpl**
```java
@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }
}
```

**ExecutionTest**
```java
@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod = {}", helloMethod);
    }
}
```

`AspectJExpressionPointcut`이 포인트컷 표현식을 처리해주는 클래스다. 여기에 포인트컷 표현식을 지정하면 된다. `AspectJExpressionPointcut`는 상위에 `Pointcut` 인터페이스를 가진다.
  
`printMethod()` 테스트는 `MemberServiceImpl.hello(String)` 메서드의 정보를 출력해준다.
테스트 코드를 실행한 결과는 아래와 같다.

```shell
helloMethod = public java.lang.String com.roy.spring.myaop.member.MemberServiceImpl.hello(java.lang.String)
```

---

### execution - 1

execution 문법은 아래와 같다.

```shell 
execution(modifiers-pattern? ret-type-patern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)

execution(접근제어자? 변환타입 선언타입?메서드이름(파라미터) 예외?)
```

- 메소드 실행 조인 포인트를 매칭한다.
- ?는 생략할 수 있음을 의미한다.
- `*` 같은 패턴을 지정할 수 있다.

---

#### 가장 정확한 포인트컷

먼저 `MemberServiceImpl.hello(String)` 메소드와 가장 정확하게 모든 내용이 매칭되는 표현식이다.

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String com.roy.spring.myaop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

- `AspectJExpressionPointcut`에 `pointcut.setExpression`을 통해서 포인트컷 표현식을 적용할 수 있다.
- `pointcut.matches(메서드, 대상 클래스)`를 실행하면 지정한 포인트컷 표현식의 매칭 여부를 `true`, `false`로 반환한다.
- 위의 예시에서 매칭되는 조건은 아래와 같다.
  - 접근제어자?: `public`
  - 반환타입: `String`
  - 선언타입?: `com.roy.spring.myaop.member.MemberServiceImpl`
  - 메서드이름: `hello`
  - 파라미터: `(String)`
  - 예외?: 생략
- `MemberServiceImpl.hello(String)`메서드와 포인트컷 표현식의 모든 내용이 정확하게 일치하기 때문에 `true`를 반환한다.

---

#### 가장 많이 생략된 포인트컷

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

- 생략할 수 있는 부분을 모두 생략한 포인트컷으로 매칭 조건은 아래와 같다.
  - 접근제어자?: 생략
  - 반환타입: `*`
  - 선언타입?: 생략
  - 메서드이름: `*`
  - 파라미터: `(..)`
  - 예외?: 없음
- `*`은 아무 값이나 들어와도 된다는 의미이다.
- 파라미터에서 `..`은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다.

---

#### 메서드 이름 매칭 관련 포인트컷

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void nameMatch() {
      pointcut.setExpression("execution(* hello(..))");
      assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
  
    @Test
    void nameMatchStart1() {
      pointcut.setExpression("execution(* hel*(..))");
      assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
  
    @Test
    void nameMatchStart2() {
      pointcut.setExpression("execution(* *el*(..))");
      assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
  
    @Test
    void nameMatchFalse() {
      pointcut.setExpression("execution(* not(..))");
      assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
```

---

#### 패키지 매칭 관련 포인트컷

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    
    @Test
    void packageExactFalse() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
    
    @Test
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    
    @Test
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* com.roy.spring.myaop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

- `com.roy.spring.myaop.member.*(1).*(2)`에서 (1)은 타입을 의미하며, (2)는 메서드 이름을 의미한다.
- 패키지에서 `.`, `..`은 차이가 있으므로 이해해야 한다.
  - `.`: 정확하게 해당 위치의 패키지를 의미한다.
  - `..`: 해당 위치의 패키지와 그 하위 패키지도 포함한다.

---

### execution - 2

#### 타입 매칭 - 부모 타입 허용

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

`typeExactMatch()`는 타입 정보가 정확하게 일치하기 때문에 매칭된다.  
`typeMatchSuperType()`을 주의해서 확인해야 한다.  
`execution`에서는 `MemberService`처럼 부모 타입을 선언해도 그 자식 타입은 매칭된다. 다형성에서 `부모타입 = 자식타입`이 할당 가능하다는 점과 유사하다.

---

#### 타입 매칭 - 부모 타입에 있는 메서드만 허용

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* com.roy.spring.myaop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }
}
```

`typeMatchInternal()`의 경우 `MemberServiceImpl`를 표현식에 선언했기 때문에 그 안에 있는 `internal(String)` 메서드도 매칭 대상이 된다.  
`typeMatchNoSuperTypeMethodFalse()`를 주의해서 확인해야 한다.  
이러한 경우 표현식에 부모 타입인 `MemberService`를 선언했다. 자식 타입인 `MemberServiceImpl`의 `internal(String)` 메소드를 매칭하려 한다. 
하지만 `MemberService` 인터페이스에는 `internal(String)` 메서드가 없다.  
  
부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다. 
그래서 부모 타입에 있는 `hello(String)` 메서드는 매칭에 성공하지만, 부모 타입에 없는 `internal(String)`는 매칭에 실패한다.

---

#### 파라미터 매칭

```java
@Slf4j
public class ExecutionTest {
    // ...
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
    
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
    
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
```

`execution` 파라미터 매칭 규칙은 아래와 같다.
- `(String)`: 정확하게 String 타입의 파라미터
- `()`: 파라미터가 없어야 한다.
- `(*)`: 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
- `(*, *)`: 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
- `(..)`: 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 파라미터가 없어도 되며, `0..*`로 이해하면 된다.
- `(String, ..)`: String 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
  - 예) `(String)`, `(String, Xxx)`, `(String, Xxx, Xxx)` 허용

---

**참고한 자료**:

- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2