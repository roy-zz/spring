이번 장에서는 다양한 의존관계 주입 방법에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 의존관계 주입 방법

의존관계 주입(Dependency Injection)에는 크게 4가지 방법이 있다.

- **생성자 주입(Constructor Injection)**
- **수정자 주입(Setter Injection)**
- **필드 주입(Field Injection)**
- **일반 메서드 주입**

일반적인 상황이라면 생성자 주입을 사용해야하며 특별한 상황에만 수정자 주입을 사용해야한다.
필드 주입은 테스트 코드를 작성하는 경우에만 사용한다.
왜 이러한 결론을 내리게 되었는지 하나씩 알아보도록 한다.

---

#### 생성자 주입(Constructor Injection)

생성자를 통해서 주입받는 방식이며 생성자 호출 시점에 딱 한 번만 호출되는 것을 보장한다.
**불변, 필수** 의존관계에 사용된다. 우리는 대부분 불변이며 필수인 값을 주입받아서 사용하기 때문에 대부분의 경우 생성자 주입을 사용하게 된다.

OrderServiceImpl을 예로 들어 생성자 주입하는 방법을 알아본다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;
    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
}
```

@Component 어노테이션을 사용하여 컴포넌트 스캔의 대상임을 지정하였다.
주입받아야하는 필드에 final 키워드를 사용하였고 생성 시점에 주입받도록 하였다.
생성자에는 @Autowired 어노테이션을 사용하여 DI 컨테이너에게 필요한 빈을 주입받도록 유도하였다.

**생성자가 하나밖에 없다면 @Autowired를 생략해도 스프링에서 자동으로 주입**해준다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
}
```

---

#### 수정자 주입(Setter Injection)

우리가 일반적으로 칭하는 Getter, Setter의 Setter 메서드를 사용하여 필드 값을 주입받는 방법이다.
생성자 주입과 다르게 **선택, 변경**의 가능성이 있는 의존관계에 사용한다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private DiscountPolicy discountPolicy;
    private MemberRepository memberRepository;
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

생성자 주입과 다르게 객체 생성 시점에는 필드의 값이 없기 때문에 final 키워드를 사용할 수 없다.
@Autowired는 기본적으로 주입할 대상이 없으면 오류가 발생하기 때문에 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false)로 지정해야한다.

---

#### 필드 주입(Field Injection)

필드에서 바로 주입하는 방법이며 가장 간단하게 의존관계를 주입할 수 있는 방법이다.
테스트를 할 때 필드의 값들을 Mock 객체로 대체하여 테스트를 진행해야 하는데 필드 주입은 불가능하다.
물론 리플렉션을 사용하여 수정하는 것이 불가능 한 것은 아니지만 테스트를 위해 추가로 해야하는 작업의 양이 상당해진다.
DI 프레임워크가 없으면 의존관계 주입이 불가능해진다.

필자는 무엇보다 IntelliJ에서 사용하지 말라고 노란줄을 긋는데 보기 싫어서 사용을 피하게 된다.

애플리케이션의 서비스 코드와는 무관한 테스트 코드에서 빠르게 테스트 코드를 작성하기 위해 사용하거나 스프링의 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용해야한다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    @Autowired private DiscountPolicy discountPolicy;
    @Autowired private MemberRepository memberRepository;
}
```

---

#### 일반 메서드 주입

init와 같이 초기화를 담당하는 메서드를 생성하고 주입받는 방법이다.
수정자 주입과는 다르게 한번에 많은 필드를 주입 받을 수 있지만 일반적으로 잘 사용되지 않는 방법이다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private DiscountPolicy discountPolicy;
    private MemberRepository memberRepository;
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

DI 컨테이너에 주입할 빈이 없더라도 동작해야할 때가 있다.
이런 상황에서 @Autowired만 사용한다면 requried의 옵션의 기본값이 true이기 때문에 오류가 발생한다.
아래와 같은 방식으로 주입할 빈이 없는(Null) 상황을 대처할 수 있다.

```java
@Autowired(required = false)
public void setMemberRepository(MemberRepository memberRepository) {
    // 호출되지 않음    
}

@Autowired
public void setMemberRepository(@Nullable MemberRepository memberRepository) {
    // Null 호출
}

@Autowired
public void setMemberRepository(Optional<MemberRepository> memberRepository) {
    // Optional.empty 호출    
}
```

---

### 의존관계 주입 방식 선택 기준

**생성자 주입을 선택한다.**

우리가 개발하는 어플리케이션은 일반적으로 실행되고 종료되는 시점까지 의존관계가 변경되는 일이 없으며 오히려 변경된다면 오류가 발생할 수 있다.
수정자 주입의 경우도 테스트 코드를 작성할 때 유연하게 사용할 수 있을 것처럼 보이지만 수정자(Setter)를 public으로 열어두어야 하기 때문에 주입받은 빈이 언제든지 변경될 수 있다.
생성자 주입은 객체를 생성할 때 딱 한 번만 호출되는 것이 보장되므로 객체를 Immutable하게 설계할 수 있다.

**final 키워드**

OrderServiceImpl 코드를 살펴보면 필드에 final 키워드를 사용하였다.
혹시라도 생성자에서 값이 설정되지 않는다면 컴파일 시점에 오류가 발생한다.
컴파일 시점에 발생하는 오류가 가장 발견하기 쉬우며 수정하기도 쉬운 오류다.

```java
@Component
public class OrderServiceImpl implements OrderService {

    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;

    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
}
```

하지만 어플리케이션의 실행 중간에 MemberRepository가 변경될 가능성이 있다면 생성자 주입과 수정자 주입을 같이 사용하면 된다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final DiscountPolicy discountPolicy;
    private MemberRepository memberRepository;
    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
```

이러한 방식으로 우리는 거의 모든 경우에 **생성자 주입만 사용**해야하며 간혹 수정이 가능해야하는 빈이 있는 경우에만 수정자 주입을 섞어서 사양한다.

---

### 롬복 적용

Spring + Java 조합의 개발환경이라면 대부분의 서비스가 롬복을 사용하고 있을 것이다.
롬복을 사용하고 있다면 생성자 주입도 필드 주입만큼 간단하게 코드를 구성할 수 있다.

**수정 전 OrderServiceImpl**
```java
@Component
public class OrderServiceImpl implements OrderService {
    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;
    @Autowired
    public OrderServiceImpl(DiscountPolicy discountPolicy, MemberRepository memberRepository) {
        this.discountPolicy = discountPolicy;
        this.memberRepository = memberRepository;
    }
}
```

**수정 후 OrderServiceImpl**
```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final DiscountPolicy discountPolicy;
    private final MemberRepository memberRepository;
}
```

@RequiredArgsConstructor 어노테이션이 추가되었고 생성자와 @Autowired 어노테이션이 제거되었다.

생성자 주입의 경우 생성자가 단 하나인 상황이라면 @Autowired를 생략할 수 있다.
롬복의 @RequiredArgsConstructor는 final 키워드가 붙어있거나 @NotNull인 필드 값을 파라미터로 받는 생성자를 만들어준다.

두 가지 기능을 사용하여 필드 주입보다 간략하면서도 유연한 생성자 주입을 구현하게 되었다.

---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/