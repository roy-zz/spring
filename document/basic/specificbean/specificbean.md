이번 장에서는 [다양한 의존관계 주입 방법(링크)](https://imprint.tistory.com/171)에 이어 중복 빈 처리에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 조회 된 빈이 2개 이상인 경우

우리는 아래와 같이 DiscountPolicy를 구현한 두 개의 할인 정책이 있다.

```java
@Component
public class FixedDiscountPolicy implements DiscountPolicy {}
@Component
public class RatioDiscountPolicy implements DiscountPolicy {}
```

이렇게 동일한 역할(Interface)을 구현한 구현체가 복수개인 상황에서 @Autowired를 사용하면 NoUniqueBeanDefinitionException이 발생한다.

```java
@Autowired
private DiscountPolicy discountPolicy;
```

```bash
NoUniqueBeanDefinitionException: No qualifying bean of type 'com.roy.service.DiscountPolicy' 
available: expected single matching bean but found 2: fixedDiscountPolicy, ratioDiscountPolicy
```

하나의 빈이 매칭될 것이라고 예상했지만 fixedDiscountPolicy와 ratioDiscountPolicy 두 개를 찾게되었다는 메시지다.
지금부터 복수개의 빈이 검색되었을 때를 해결하는 방법에 대해서 알아본다.

---

### 동일한 타입 여러 개의 빈을 다루는 방법

동일한 타입의 빈을 다루는 방법에는 크게 세가지가 있다.

- **@Autowired 필드 명**
- **Qualifier**
- **Primary**

#### @Autowired 필드 명

@Autowired의 경우 타입으로 매칭을 시도하고 만약 여러개의 빈이 있으면 필드의 이름과 매칭되는 빈으로 주입한다.

문제가 발생했던 코드는 아래와 같다.

```java
@Autowired
private DiscountPolicy discountPolicy;
```

여기서 필드명을 우리가 원하는 빈의 이름과 매칭시켜준다.

```java
@Autowired
private DiscountPolicy ratioDiscountPolicy;
```

정상적으로 ratioDiscountPolicy 빈이 주입되었고 오류는 발생하지 않는다.

---

#### @Qualifier

@Qualifier는 구분하는 기준을 추가하는 것일뿐 빈의 이름을 변경하는 방법은 아니다.
문제가 발생하던 RatioDiscountPolicy와 FixedDiscountPolicy 코드를 아래와 같이 수정한다.

```java
@Component
@Qualifier("priorityDiscountPolicy")
public class RatioDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("secondaryDiscountPolicy")
public class FixedDiscountPolicy implements DiscountPolicy {}
```

구분하는 기준이 추가되었기 때문에 사용하는 쪽에서도 기준을 추가해서 주입받아야한다.

**생성자 자동 주입인 경우**
```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, 
                        @Qualifier("priorityDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

**수정자 자동 주입인 경우**
```java
@Autowired
public DiscountPolicy setDiscountPolicy(@Qualifier("priorityDiscountPolicy") DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy; 
}
```

@Qualifier("priorityDiscountPolicy")와 같이 어노테이션을 사용하였지만 빈을 찾지 못한다면 priorityDiscountPolicy라는 이름을 가진 빈을 찾기시작한다.
하지만 이는 추가로 제공하는 기능일 뿐이며 @Qualifier의 본연의 기능만 사용하는 것이 명확하다.

@Qualifier의 작동방식 순서는 아래과 같다.

1. @Qualifier간의 매칭
2. @Qualifier의 속성으로 매칭되는 빈이 없다면 빈의 이름과 매칭 시도
3. 1, 2단계 모두 실패한다면 NoSuchBeanDefinitionException 예외 발생

---

### @Primary

@Primary는 같은 타입의 빈들에게 우선순위를 지정하는 방법이다.
여러개의 동일한 타입 빈이 검색된다면 @Primary가 우선권을 가진다.

문제가 발생했던 코드에 @Primary 어노테이션을 사용하여 사용하려는 빈이 우선권을 가지도록 수정해본다.

```java
@Primary
@Component
public class RatioDiscountPolicy implements DiscountPolicy {}

@Component
public class FixedDiscountPolicy implements DiscountPolicy {}
```

@Qualifier와는 다르게 사용하는 쪽의 코드는 변경사항이 없다.

**생성자를 사용하는 경우**
```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

**수정자를 사용하는 경우**
```java
@Autowired
public DiscountPolicy setDiscountPolicy(DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;    
}
```

---

### @Primary, @Qualifier의 활용

@Primary와 @Qualifier의 차이에 대해서 알아보았다. 
사용하는 쪽의 코드 수정이 없다는 이유로 @Primary가 좋다고 할 수는 없다.
@Primary는 단순히 하나의 구현체에게 우선순위를 지정한 것이고 @Qualifier는 상세하게 구분가능한 값을 지정한 것이다.

아래와 같이 다중 DataSource를 사용하는 상황을 가정해본다.
DataSource를 구현한 세 개의 구현체가 있다.

```java
@Primary
@Component
public RDBDataSource implements DataSource {}

@Component
@Qualifier("mongo")
public MongoDBDataSource implements DataSource {}

@Component
@Qualifier("redis")
public RedisDataSource implements DataSource {}
```

일반적인 상황에서는 RDB 데이터 소스를 사용하고 MongoDB가 필요한 시점에는 사용하는 쪽에서 @Qualifier("mongo")를 명시하여 MongoDB를 사용한다.
동일한 방식으로 Redis를 사용해야하는 시점에는 사용하는 쪽에서 @Qualifier("redis")와 같이 사용하여 기본값 & 옵션값과 같은 개념으로 사용할 수 있다.

---

### 커스텀 어노테이션

@Qualifier를 사용한 방법의 치명적인 단점은 문자열을 사용하는 방식이기 때문에 컴파일 시점에 타입 체크가 되지 않는다는 점이다.
아래와 같이 @Qualifier 속성을 가지고 있는 어노테이션을 직접 생성하여 단점을 보완할 수 있다.

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("priorityDiscountPolicy")
public @interface PriorityDiscountPolicy {}
```

**생성자 주입인 경우**

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @PriorityDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

**수정자 주입인 경우**

```java
@Autowired
public DiscountPolicy setDiscountPolicy(@PriorityDiscountPolicy DiscountPolicy discountPolicy) {
    this.discountPolicy = discountPolicy;    
}
```

어노테이션에는 상속이라는 개념이 없다. 
모두 스프링에서 지원해주는 기능이며 @Qualifier 뿐만 아니라 다른 어노테이션들도 이러한 방식으로 재정의하여 사용할 수 있다. 

---

### 조회된 동일 타입 빈이 모두 필요한 경우

예시를 위해 아래와 같은 테스트 코드를 작성한다.

```java
public class AllBeanTest {
    @Test
    @DisplayName("동일 타입 다중 빈 검색 테스트")
    void findAllBeanTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(
                AutoSpringApplicationConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(0L, "Roy", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixedDiscountPolicy");

        assertTrue(discountService instanceof DiscountService);
        assertEquals(1000, discountPrice);
    }
    
    static class DiscountService {
        private final Map<String, DiscountPolicy> mapOfPolicies;
        private final List<DiscountPolicy> listOfPolicies;

        public DiscountService(Map<String, DiscountPolicy> mapOfPolicies, List<DiscountPolicy> listOfPolicies) {
            this.mapOfPolicies = mapOfPolicies;
            this.listOfPolicies = listOfPolicies;
            System.out.println("mapOfPolicies = " + mapOfPolicies);
            System.out.println("listOfPolicies = " + listOfPolicies);
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = mapOfPolicies.get(discountCode);
            System.out.println("discountCode = " + discountCode);
            System.out.println("discountPolicy = " + discountPolicy);
            return discountPolicy.discount(member, price);
        }
    }
}
```

DiscountService는 Map으로 검색된 모든 DiscountPolicy를 주입받고 fixedDiscountPolicy와 ratioDiscountPolicy가 주입된다.
DI 컨테이너는 생성자에게 클래스 정보를 받는다. 여기서 클래스 정보로 넘어간 클래스들은 스프링 빈으로 자동 등록된다.

```java
new AnnotationConfiApplicationContext(AutoSpringApplicationConfig.class, DiscountService.class);
```

- new AnnotationConfigApplicationContext()를 통해 DI 컨테이너를 생성한다.
- AutoSpringApplicationConfig와 DiscountService를 파라미터로 넘기면서 해당 클래스를 자동으로 스프링 빈으로 등록한다.
- 클라이언트는 DiscountService에게 "fixedDiscountPolicy"라는 문자를 전달하였고 이에 맞는 전략인 FixedDiscountPolicy를 사용하였다. 
  간단한 전략패턴이라고 볼 수 있다.

---

### 자동 주입 vs 수동 주입의 기준

결론부터 말하면 **모든 곳에 자동 주입을 사용**하고 특정한 상황에만 수동 주입을 사용해야한다.
특히 최근에는 스프링에서도 @Component, @Controller, @Service, @Repository등을 사용하며 자동 주입 사용을 유도하고 있다.
무엇보다 자동 주입을 사용하더라도 OCP와 DIP를 위반하지 않도록 이미 스프링에서 기반을 다져놓았다.

그러면 특정 상황에서만 수동 주입을 사용한다고 하였는데 어떠한 상황인지 알아보도록 한다.

---

#### 기술 지원 빈을 구성하는 경우에 사용

어플리케이션은 크게 업무 로직과 기술 지원 로직으로 나뉘어진다.

- 업무 로직 빈: @Controller, @Service, @Repository와 같이 일반적으로 서비스 로직을 가지고 있는 도메인 친화적인 빈이다.
  비즈니스 요구사항에 맞게 추가 및 수정된다.
- 기술 지원 빈: 어플리케이션을 구성하는 전반적인 기술을 담당한다. 예를 들어 공통 관심사(AOP)를 처리한다거나 DB Connection, 공통 로그처리와 같이
  업무 로직을 지원하기 위한 기능을 한다.

**업무 로직**의 경우 이를 구성하는 빈들의 수도 상당히 많고 유사한 패턴으로 구성되어 있기 때문에 자동 주입을 사용하는 것이 개발 생산성을 높여준다.
**기술 지원 로직**의 경우 업무 로직과 비교하여 수는 적지만 어플리케이션의 전반에 결쳐서 광범위하게 영향을 미친다.
또한 문제가 발생하였을 때 어디서 발생하였는지 찾기가 쉽지않다. 이런 기술 지원 로직들은 수동 빈으로 등록하여 한 번에 명확하게 구성하는 것이 좋다.

---

#### 비즈니스 로직 중에서 다형성을 적극 활용하는 경우

예를 들어 우리가 지금까지 사용하던 DiscountPolicy가 두 개가 아니라 5개까지 늘어나는 상황이라고 가정해본다.

```java
@Component
public class ADiscountPolicy implements DiscountPolicy {}
@Component
public class BDiscountPolicy implements DiscountPolicy {}
@Component
public class CDiscountPolicy implements DiscountPolicy {}
@Component
public class DDiscountPolicy implements DiscountPolicy {}
@Component
public class EDiscountPolicy implements DiscountPolicy {}
```

우리는 DiscountPolicy 인터페이스를 통해서 모든 하나하나 찾아가면서 모든 구현체를 확인해야한다.
하나하나 찾아주는 것도 IDE의 기능이므로 IDE가 없다면 쉽지 않은 작업이 된다.

하지만 아래와 같이 별도의 설정 정보로 수동 등록을 한다면 설정 파일을 확인하는 것만으로도 구현체를 모두 한 번에 확인할 수 있다.
또한 이렇게 같은 카테고리로 묶여있는 타입들은 동일한 패키지로 관리해야 추후 유지보수에 유리하다.

```java
import java.beans.BeanProperty;

@Configuration
public class DiscountPolicyConfig {
    @Bean
    public DiscountPolicy aDiscountPolicy() {
        return new ADiscountPolicy();
    }
    @Bean
    public DiscountPolicy bDiscountPolicy() {
        return new BDiscountPolicy();
    }
    @Bean
    public DiscountPolicy cDiscountPolicy() {
        return new CDiscountPolicy();
    }
    @Bean
    public DiscountPolicy dDiscountPolicy() {
        return new DDiscountPolicy();
    }
    @Bean
    public DiscountPolicy eDiscountPolicy() {
        return new EDiscountPolicy();
    }
}
```

---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/