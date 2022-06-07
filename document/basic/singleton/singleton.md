이번 장에서는 스프링 빈과 싱글톤에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 싱글톤(Singleton)

#### 싱글톤이 적용되지 않은 순수 자바 DI 컨테이너

스프링은 기업용 온라인 서비스 기술을 지원하기 위해 탄생하였다.
이러한 웹 애플리케이션은 일반적으로 동시에 여러 고객의 요청을 처리해야한다.

아래는 스프링을 사용하지 않은 순수 자바를 사용한 DI 컨테이너 코드다.

```java
public class PureApplicationConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    public OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
```

코드만 봐도 알 수 있듯이 MemberService를 조회할 때마다 새로운 MemberServiceImpl과 MemoryMemberRepository가 생성된다.
정말 새로 생성되는지 테스트하는 코드는 아래와 같다.

```java
public class SingletonTest {
    @Test
    @DisplayName("순수 자바 DI 컨테이너 Non Singleton 테스트")
    void pureJavaDiContainerNonSingletonTest() {
        PureApplicationConfig pureApplicationConfig = new PureApplicationConfig();
        MemberService memberService1 = pureApplicationConfig.memberService();
        MemberService memberService2 = pureApplicationConfig.memberService();
        assertNotEquals(memberService1, memberService2);
    }
}
```

테스트 결과는 성공이며 MemberService는 DI 컨테이너에서 가져올 때마다 새로운 객체가 생성된다는 것을 알 수 있다.

---

#### 싱글톤 서비스

싱글톤에 대해서 자세히 알아보기 위해 싱글톤 서비스를 생성해본다.

```java
public class SingletonService {

    private static final SingletonService INSTANCE = new SingletonService();

    public static SingletonService getInstance() {
        return INSTANCE;
    }

    private SingletonService() {}

    public void logic() {
        System.out.println("싱글톤 로직 실행");
    }
}
```

1. 클래스가 로드 되는 시점에 스태틱 영역에 SingletonService를 생성한다.

2. SingletonService를 사용하려는 클라이언트는 객체를 생성하는 것이 아니라 getInstance()를 호출하여 스태틱 영역에 존재하는 객체를 사용한다.

3. 클라이언트가 직접 new를 사용하여 생성하는 것을 막기 위하여 기본으로 생성되는 생성자를 private으로 접근 제한하였다.

---

싱글톤 서비스가 정말로 하나의 객체만 사용되는지 테스트하는 코드는 아래와 같다.

```java
public class SingletonTest {
    @Test
    @DisplayName("싱글톤 서비스 고유 객체 확인 테스트")
    void singletonServiceUniqueInstanceTest() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();
        assertEquals(singletonService1, singletonService2);
    }
}
```

테스트 코드는 성공할 것이며 몇 번을 요청하더라도 동일한 객체가 반환되는 것을 알 수 있다.

---

#### 싱글톤 패턴의 제약

싱글톤 패턴은 안티 패턴으로 분류될 만큼 관리 포인트 및 문제점이 많은 패턴이다.

1. 싱글톤을 구현하기 위해서 클래스 내부에 자신을 생성하는 코드, new를 방지하는 코드등 부수적인 코드들이 등장한다.

2. 싱글톤 객체를 사용하는 클라이언트는 구현체인 싱글톤 클래스를 의존하면서 DIP를 위반하게 된다.

3. DIP를 위반하게 되면서 추후 수정사항이 발생하는 경우 OCP 원칙 또한 위반하게 된다.

4. 서비스의 여러 곳에서 사용되고 있으므로 속성을 변경하거나 초기화가 어렵다.

5. private 생성자를 사용해야 하므로 자식 클래스를 만들기 어렵다.

이렇게 많은 제약들은 결국 변경의 유연성이 떨어진다는 결과를 가지고 온다.

---

#### 싱글톤 컨테이너

싱글톤으로 메모리 낭비를 막는 것은 좋지만 제약 사항이 많아서 실제로 사용하기는 어렵다.
이러한 문제를 인지한 스프링 개발자들은 스프링 컨테이너가 싱글톤의 문제점을 해결하면서 장점인 인스턴스를 싱글톤으로 1개만 생성해서 관리하도록 구현하였다.

스프링 컨테이너는 우리가 싱글톤 패턴을 적용하지 않아도 **기본적으로 빈을 싱글톤**으로 관리한다.
스프링 컨테이너는 싱글톤 컨테이너 역할을 하며 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.
우리는 스프링 컨테이너가 싱글톤을 관리하는 메커니즘을 제공해주기 때문에 싱글톤 패턴의 제약이었던 지저분한 코드, DIP 및 OCP 위반, private 생성자 등의 문제점을 직접 해결하지 않고 싱글톤 빈을 사용할 수 있다.

스프링 DI 컨테이너(이하 DI 컨테이너)에 의해 관리받을 SpringApplicationConfig 클래스를 생성하였다.

```java
@Configuration
public class SpringApplicationConfig {
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
```

DI 컨테이너가 정말 싱글톤으로 빈을 관리하는지 테스트하는 코드는 아래와 같다.

```java
public class SingletonTest {
    @Test
    @DisplayName("싱글톤 컨테이너 고유 객체 확인 테스트")
    void springContainerSingletonTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        assertEquals(memberService1, memberService2);
    }
}
```

DI 컨테이너의 이러한 속성 덕분에 우리는 고객의 요청이 올 때 새로운 객체를 생성하는 것이 아니라 이미 만들어진 객체를 재사용할 수 있다.
DI 컨테이너가 기본적으로 싱글톤으로 빈을 관리하는 것은 맞지만 싱글톤만 지원하는 것은 아니며 필요에 따라 새로운 객체를 생성해서 반환하는 기능도 제공한다.

---

#### 싱글톤 빈 사용시 주의사항

DI 컨테이너에 의해 관리되는 싱글톤 빈은 일반적인 싱글톤 객체와 동일하게 주의해야하는 점이 있다.
여러 클라이언트에서 하나의 객체에 접근하기 때문에 싱글톤 객체가 상태를 유지(stateful)하도록 설계하면 안된다.
즉, 싱글톤 빈을 찍어내는 클래스는 무상태(stateless)로 설계해야하며 특정 클라이언트에 의존적인 필드가 있으면 안된다.
가능하면 필드 변수 값은 ReadOnly로 사용하고 Thread간에 공유되지 않는 지역변수, 파라미터 ThreadLocal등을 사용해야한다.

상태를 유지(stateful)하도록 설계하면 어떠한 문제가 발생하는지 확인하기 위해 아래와 같은 서비스를 하나 생성한다.

우리는 price를 static으로 만들지 않았다. 그렇기 때문에 객체간에 공유가 되지 않을 것으로 예상하고 있을 것이다.

```java
public class StatefulService {

    private int price;

    public void order(String name, int price) {
        System.out.println("name: " + name + ", price: " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
```

우리의 예상과 다르게 아래의 테스트 코드는 실패한다.
분명 객체간의 공유되지 않도록 price를 static으로 선언하지 않았다.
맞다. 우리의 의도대로 price는 객체간의 공유가 되지 않지만 싱글톤 객체 단 하나만 사용하기 때문에 발생한 것이다.

```java
public class SingletonTest {
    @Test
    @DisplayName("싱글톤 필드 공유 오류 발생 확인 테스트")
    void singletonSharingFieldOccurExceptionTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringApplicationConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        statefulService1.order("Roy", 10000);
        statefulService2.order("Perry", 20000);

        assertEquals(10000, statefulService1.getPrice());
    }
}
```

이러한 이유로 공유 필드의 사용은 항상 조심해야한다.
다중 Thread 환경에서는 더욱 많은 위험이 숨어있다.

---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/