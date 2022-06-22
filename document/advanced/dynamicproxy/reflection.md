이번 장에서는 동적 프록시를 이해하기 위한 선수 지식인 **자바의 리플렉션**에 대해서 알아보도록 한다.  
모든 코드는 [깃허브(링크)](https://github.com/roy-zz/spring) 에 올려두었다.

---

### 개요

우리는 이전에 [인터페이스 기반 프록시](https://imprint.tistory.com/288) 와 [구체 클래스 기반 프록시](https://imprint.tistory.com/289) 를 적용해 보면서 프록시 대상 클래스 또는 인터페이스마다 프록시 클래스를 만들어야 하는 문제를 보았다.  
또한 우리가 생성한 프록시 클래스는 대부분 비슷한 모양을 하고 있었다. 당연히 우리의 선배 개발자들은 이러한 문제점을 해결하기 위해 많은 노력을 하였고 자바의 `JDK 동적 프록시`와 오픈소스인 `CGLIB` 라이브러리를 사용하면 동적으로 프록시 객체를 생성할 수 있다.  
동적으로 프록시 객체를 적용하게 되면 실제 클래스를 대상으로 모든 프록시 클래스를 생성하는 것이 아니라 프록시를 적용할 코드를 만들어서 동적 프록시 기술로 프록시 객체를 동적으로 생성하게 된다.
  
이번 장에서는 동적 프록시 기술의 기본 지식이 되는 **자바의 리플렉션**에 대해서 학습해보도록 한다.

---

### 리플렉션(반영, Reflection)
  
> 반영(reflection)은 컴퓨터 과학 용어로, 컴퓨터 프로그램에서 런타임 시점에 사용되는 자신의 구조와 행위를 관리하고 수정할 수 있는 프로세스를 의미한다.  
> "type introspection"은 객체 지향 프로그램언어에서 런타임에 객체의 형(type)을 결정할 수 있는 능력을 의미한다.  
> ...
> 반영은 런타임에 프로그램의 수행을 수정하고, 관찰하기 위하여 사용할 수 있다. 반영 지향적인 프로그램 구성 요소는 내부 코드의 실행을 감시할 수 있고, 구성 요소 자신의 궁극적인 목표에 맞도록 내부를 수정 할 수 있다.  
> 이는 전형적으로 런타임에 프로그램 코드를 동적으로 할당하여 이루어진다.
<center>위키백과</center>

어려운 말들이 많이 나와 있지만 중요한 핵심은 런타임에 **프로그램 코드를 동적으로 할당**한다는 점이다.  
모든 프로그램은 런타임 시점에 코드가 메모리에 적재되고 실행된다. 리플렉션은 이렇게 메모리에 적재된 코드를 동적으로 변경하는 기술이다.
  
자바에서의 리플렉션(Reflection)은 객체를 통해 클래스의 정보를 분석하는 `API`며 조회할 수 있는 정보는 아래와 같다.  

- 클래스 이름
- 클래스 수식어(접근제한자, synchronized 등)
- Package 정보
- 부모 클래스
- 구현하고 있는 인터페이스
- 생성자
- 메서드 
- 애노테이션
  
객체의 구조에 대해 동적 검색을 런타임에 지원하며 클래스의 타입을 알지 못하더라도 메소드, 타입, 변수에 접근할 수 있도록 해준다.  
자바 클래스 파일은 바이트 코드로 컴파일되어 JVM의 스태틱영역에 적재되고 클래스의 이름만 알고 있다면 바이트 코드에서 클래스에 대한 정보를 가져오게 된다.  
  
---

### 리플렉션 테스트

직접 테스트 코드를 작성하면서 자바의 리플렉션이 어떠한 원리로 작동하는지에 대해서 알아본다.  
아래는 리플렉션이 적용되기 전의 테스트 코드다.  
`Friend`라는 내부 클래스가 있고 `callRoy()` 메서드와 `callPerry()`라는 이름이 다른 두개의 메서드를 가지고 있다.  
메서드의 이름이 다르기 때문에 우리는 원하는 출력을 위해서 서로 다른 메서드를 호출해야 한다.
  
```java
@Slf4j
public class ReflectionTest {

    @Test
    void notApplyReflectionTest() {
        Friend target = new Friend();

        log.info("Start");
        String result1 = target.callRoy();
        log.info("result1 = {}", result1);

        log.info("Start");
        String result2 = target.callPerry();
        log.info("result2 = {}", result2);
    }
    
    @Slf4j
    static class Friend {
        public String callRoy() {
            log.info("Call Roy");
            return "ROY";
        }
        public String callPerry() {
            log.info("Call Perry");
            return "PERRY";
        }
    }
}
```
  
우리는 위의 코드에서 중복되는 부분을 하나로 합치려고 하더라도 메서드의 이름이 다르기 때문에 중복되는 부분을 하나의 코드로 합치는 것은 쉽지 않다.  
만약 메서드를 직접 코드로 작성하여 호출하는 것이 아니라 런타임에 변할 수 있는 문자열로 호출할 수 있다면 동적으로 메서드 호출이 가능해진다.  
이러한 기능을 제공하는 것이 **자바의 리플렉션**이다.
  
아래의 코드는 리플렉션을 사용해서 직접 코드에서 메서드를 호출하는 것이 아니라 문자로 원하는 메서드명을 입력하여 필요한 메서드를 가져오는 테스트 코드다.
  
```java
@Test
void applyReflectionTest() throws Exception {
    Class<?> classFriend = Class.forName("com.roy.spring.myproxy.jdkdynamic.ReflectionTest$Friend");

    Friend target = new Friend();

    Method methodCallRoy = classFriend.getMethod("callRoy");
    Object result1 = methodCallRoy.invoke(target);
    log.info("result1 = {}", result1);

    Method methodCallPerry = classFriend.getMethod("callPerry");
    Object result2 = methodCallPerry.invoke(target);
    log.info("result2 = {}", result2);
}
```

- Class.forName(...) 메서드를 통해서 원하는 클래스의 메타정보를 획득한다.
- {클래스 메타정보}.getMethod(...) 메서드를 통해서 원하는 메서드의 메타정보를 획득한다.
- {메서드 메타정보}.invoke(인스턴스)를 통해서 파라미터로 전달한 인스턴스에서 메서드 정보를 찾아서 실행한다. 
  
코드 자체가 짧아진 것은 아니지만 중요한 점은 런타임 시점에 변경 가능한 문자열로 동적으로 메서드 정보를 가져와서 테스트 코드가 실행시키는 메서드의 정보를 결정했다는 점이다.
  
문자열로 메서드를 호출할 수 있기 때문에 이제 중복되는 부분을 공통 로직으로 추출하여 관리할 수 있다.

```java
@Test
void extractCommonTest() throws Exception {
    Class<?> classFriend = Class.forName("com.roy.spring.myproxy.jdkdynamic.ReflectionTest$Friend");

    Friend target = new Friend();
    Method methodCallRoy = classFriend.getMethod("callRoy");
    dynamicCallMethod(methodCallRoy, target);

    Method methodCallPerry = classFriend.getMethod("callPerry");
    dynamicCallMethod(methodCallPerry, target);
}

private void dynamicCallMethod(Method method, Object target) throws Exception {
    log.info("Start");
    Object result = method.invoke(target);
    log.info("result = {}", result);
}
```

`dynamicCallMethod` 메서드는 파라미터로 메서드의 메타정보인 `Method` 객체를 전달받는다.  
어떠한 메서드의 메타정보라도 입력받을 수 있기 때문에 런타임 시점에 호출되는 메서드가 동적으로 변경된다.  
또한 모든 클래스의 최상위 클래스인 `Object`를 입력받기 때문에 어떠한 인스턴스도 입력받을 수 있다.  
결국, `dynamicCallMethod`는 어떠한 인스턴스와 메서드 메타정보가 입력되더라도 실행시킬 수 있는 메서드가 된 것이다.

---

### 정리

리플렉션은 런타임에 동작하기 때문에 문제가 있더라도 컴파일 시점에 오류를 잡을 수 없다.  
예제에서 우리는 원하는 메서드의 메타정보를 얻기위해 `callRoy`라는 문자열을 입력하였다. 하지만 문자열이 잘못되더라도 컴파일 시점에는 오류가 발생하지 않는다.  
런타임에 메서드의 메타정보를 찾으려다 찾을 수 없다는 오류가 발생할 것이다.
  
이러한 치명적인 단점으로 인해서 일반적인 상황에서는 리플렉션을 사용하지 않는다.  
우리는 컴파일러와 IDE의 힘을 빌려서 컴파일 또는 개발하는 시점에 많은 오류를 잡아왔다. 하지만 리플렉션을 사용하면 이렇게 편리한 기능을 사용할 수 없게 된다.  

---

**참고한 자료**:

- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
- https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2