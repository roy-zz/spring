이번 장에서는 [@Configuration과 싱글톤(링크)](https://imprint.tistory.com/169)에 이어 컴포넌트 스캔에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### Component Scan

우리는 지금까지 DI 컨테이너에 빈을 등록할 때 자바 코드와 XML을 사용하여 빈을 등록하였다.
하지만 이러한 방식으로 빈을 등록하게 되는 경우 실무에서 쏟아져 나오는 수많은 클래스들을 관리하기란 쉽지 않고 손이 많이 간다.
스프링에서 이러한 문제를 인지하고 편리하게 빈을 등록할 수 있도록 컴포넌트 스캔(Component Scan)이라는 기능을 제공하였다.

새로운 구성 정보를 가지는 AutoSpringApplicationConfig파일을 생성하였다.

```java
@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoSpringApplicationConfig {
    
}
```

클래스 내부에는 아무런 정보도 존재하지 않는다. 
@ComponentScan이라는 어노테이션을 사용하였고 excludeFilters를 사용하여 @Configuration 어노테이션이 사용된 클래스는 빈으로 등록하지 않겠다고 설정하였다.
이렇게 설정한 이유는 이전 장에서 사용하던 SpringApplicationConfig와 중복 빈 등록 오류를 방지하기 위해서이다.

// TODO

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
    // 생략
}
```

일반적인 상황에서는 excludeFilters를 사용할 일이 거의 없다.

우리가 빈으로 등록되기를 원하는 클래스 파일에 @Component 어노테이션을 붙여준다.

```java
@Component
public class MemoryMemberRepository implements MemberRepository {
    // 생략
}
```

```java
@Component
public class RatioDiscountPolicy implements DiscountPolicy { 
    // 생략
}
```

```java
@Component
public class MemberServiceImpl implements MemberService { 
    // 생략
}
```

@Component



---

**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/