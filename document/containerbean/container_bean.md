이번 장에서는 스프링 컨테이너와 스프링 빈에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 스프링 컨테이너

일반적으로 ApplicationContext를 스프링 컨테이너라고 한다.
정확히는 ApplicationContext는 인터페이스이며 이를 구현한 구현체들이 스프링 컨테이너가 된다.

설정(구성) 정보를 가지고 있는 ApplicationConfig클래스는 아래와 같다.

```java
@Configuration
public class ApplicationConfig {

    @Bean
    protected MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    protected OrderService orderService() {
        return new OrderServiceImpl(discountPolicy(), memberRepository());
    }

    @Bean
    protected MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    protected DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
```

스프링 컨테이너(ApplicationContext)를 생성할 때 설정 정보를 가지고 있는 클래스를 파라미터로 전달한다.

```java
ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
```

스프링 컨테이너는 파라미터로 받은 클래스 정보를 사용하여 스프링 빈을 등록한다.
ApplicationConfig 클래스를 토대로 생성된 스프링 컨테이너 테이블은 아래와 같다.

|    |    |
| -- | -- |
| memberService | MemberServiceImpl@0x01 |
| orderService | OrderServiceImpl@0x02 |
| memberRepository | MemoryMemberRepository@0x03 |
| discountPolicy | FixedDiscountPolicy@0x04 |

빈 이름의 기본값은 메서드 명을 따라가지만 @Bean(name = "memberService2")와 같이 직접 이름을 부여할 수도 있다.
만약 같은 이름의 빈이 있다면 무시되거나 기존 빈을 덮어쓰기 때문에 오류가 발생할 수 있으므로 빈 이름은 항상 다른 이름을 사용해야한다.

---

스프링 컨테이너는 파라미터 설정(ApplicationConfig.class) 정보를 참고해서 의존관계를 주입(DI)한다.

![](image/bean-relation.png)

스프링은 빈을 생성하고 의존관계를 주입하는 단계가 나누어져 있다. 
자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한 번에 처리된다.
조금 복잡해보일 수 있지만 추후에 의존관계 자동 주입을 사용하면 간단하게 의존관계 주입이 가능하다.

---

#### 컨테이너에 등록된 모든 빈 출력

```java
public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name: " + beanDefinitionName + ", object: " + bean);
        }
    }
}
```

출력 결과는 아래와 같다.

```bash
name: org.springframework.context.annotation.internalConfigurationAnnotationProcessor, object: org.springframework.context.annotation.ConfigurationClassPostProcessor@5c10f1c3
name: org.springframework.context.annotation.internalAutowiredAnnotationProcessor, object: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7ac2e39b
name: org.springframework.context.annotation.internalCommonAnnotationProcessor, object: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@78365cfa
name: org.springframework.context.event.internalEventListenerProcessor, object: org.springframework.context.event.EventListenerMethodProcessor@64a8c844
name: org.springframework.context.event.internalEventListenerFactory, object: org.springframework.context.event.DefaultEventListenerFactory@3f6db3fb
name: applicationConfig, object: com.roy.spring.configuration.ApplicationConfig$$EnhancerBySpringCGLIB$$5849a5a3@52de51b6
name: memberService, object: com.roy.spring.service.impl.MemberServiceImpl@18c5069b
name: orderService, object: com.roy.spring.service.impl.OrderServiceImpl@3a0d172f
name: memberRepository, object: com.roy.spring.repository.impl.MemoryMemberRepository@68ad99fe
name: discountPolicy, object: com.roy.spring.service.impl.FixedDiscountPolicy@485e36bc
```

org.springframework로 시작하는 빈들은 스프링에서 사용하는 빈이며 아래쪽의 빈들은 애플리케이션을 위한 빈들이다.

#### 애플리케이션 빈 출력

내가 개발하고 있는 애플리케이션에 관련된 빈만 출력하고 싶다면 아래와 같이 하면 된다.
ApplicationContext를 생성할 때 꼭 구현체인 AnnotationConfigApplicationContext를 사용해야한다.
그렇지 않으면 BeanDefinition을 사용할 수 없다.

BeanDefinition Role은 크게 두 가지가 있다.
- ROLE_APPLICATION: 직접 등록한 애플리케이션 빈
- ROLE_INFRASTRUCTURE: 스프링이 내부에서 사용하는 빈

```java
public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name: " + beanDefinitionName + ", object: " + bean);
            }
        }
    }
}
```

출력 결과는 아래와 같이 애플리케이션 관련 빈들만 출력되었다.

```bash
name: applicationConfig, object: com.roy.spring.configuration.ApplicationConfig$$EnhancerBySpringCGLIB$$5849a5a3@5c10f1c3
name: memberService, object: com.roy.spring.service.impl.MemberServiceImpl@7ac2e39b
name: orderService, object: com.roy.spring.service.impl.OrderServiceImpl@78365cfa
name: memberRepository, object: com.roy.spring.repository.impl.MemoryMemberRepository@64a8c844
name: discountPolicy, object: com.roy.spring.service.impl.FixedDiscountPolicy@3f6db3fb
```

---









---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/