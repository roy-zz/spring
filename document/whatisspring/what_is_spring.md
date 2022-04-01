이번 장에서는 면접 질문에 자주 등장하는 스프링이란 무엇인가에 대해서 알아본다.
글의 하단부에 참고한 강의와 공식문서의 경로를 첨부하였으므로 자세한 사항은 강의나 공식문서에서 확인한다.
모든 코드는 [깃허브 (링크)](https://github.com/roy-zz/spring)에 올려두었다.

---

### 스프링이란 무엇인가?

이번 장부터 몇 개의 장을 걸쳐서 스프링이란 무엇이며 무엇을 위해 탄생하였는지에 대해서 알아본다.
필자는 아직까지 "스프링이 뭐에요?" 라는 질문을 받은 적은 없다. 존경하는 W사의 모 개발자님은 기술 인터뷰에서 "스프링이 뭐에요"라는 질문을 받았고 "어노테이션의 향연입니다."이라는 답변했다고 한다.
정말 짧고 간결한 정답이라는 생각이 든다. 스프링이 어노테이션의 향연이 된 이유를 찾는다면 스프링의 탄생 배경도 알게 될 것이다.

거대해진 스프링 생태계 때문에 스프링이 왜 탄생하였는지 생각해내기란 쉽지않다. 
아마 필자도 참고한 강의가 아니었다면 무엇을 해결하기 위하여 스프링이 탄생했는지에 대해서는 생각해보지 않았을 것이다.

스프링의 탄생을 이해하기 위해서는 SOLID와 OOP의 개념에 대해서 다시 한 번 복습해야 한다.

#### SOLID

- [SRP, Single Responsibility Principle (링크)](https://imprint.tistory.com/10)
- [OCP, Open Closed Principle (링크)](https://imprint.tistory.com/11)
- [LSP, Liskov Substitution Principle (링크)](https://imprint.tistory.com/32)
- [ISP, Interface Segregation Principle (링크)](https://imprint.tistory.com/53)
- [DIP, Dependency Inversion Principle (링크)](https://imprint.tistory.com/54)

#### OOP

- [다형성, Polymorphism (링크)](https://imprint.tistory.com/9)
- [추상화, Abstraction (링크)](https://imprint.tistory.com/8)
- [상속, Inheritance (링크)](https://imprint.tistory.com/7)
- [캡슐화, Encapsulation (링크)](https://imprint.tistory.com/6)

특히 OCP, DIP, 다형성이 스프링을 탄생시킨 중요한 개념이다.

---

### 순수 자바 프로젝트

스프링없이 순수하게 자바로만 프로젝트를 만들면서 궁극적으로 어떠한 문제를 만나게 되는지 알아본다.
프로젝트 기획은 필자가 참고한 강의와 동일하게 구성한다.










**참고한 강의:** https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8

**Spring 공식 문서:** https://docs.spring.io/spring-framework/docs/current/reference/html/