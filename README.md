# 스프링 핵심 원리 기본편 내용 정리
> 김영한 강사님의 스프링 핵심원리 강의 내용을 정리한 문서입니다.
***

# 1. 객체지향형 설계와 스프링
### 1.1. 객체지향형 프로그래밍 특징 
    1. 추상화
    2. 캡슐화
    3. 상속
    4. 다형성

### 1.2. 객체지향형 프로그래밍 이란
    - 컴퓨터 프로그램을 객체 단위로 파악하고자 하는 것
    - 객체는 서로 메세지를 주고 받고 데이터를 처리하는 기능을 갖는다.

### 1.3. 다형성
    - 같은 이름으로 여러가지 기능을 만드는 것
    - 역할(인터페이스)/구현(구현체)으로 만드는 것

### 1.4. 다형성의 장점
    - 자신의 역할만 알면된다
    - 내부구조를 알 필요가 없다
    - 내부구조가 변경되어도 영향이 없다
    - 구현 대상 자체를 바꿔도 영향이 없다
* 즉 다형성으로 클라이언트를 변경하지 않아도 서버의 구현기능을 유연하게 변경이 가능하다


### 1.5. 다형성의 한계
- 인터페이스, 역할이 변경되면 많은 공수가 발생하게 된다.

### 1.6. SOLID 5원칙
    - SRP(Single Responsibility Principle) : 단일 책임의 원칙, 하나의 클래스 하나의 책임.
    - OCP(Open/Closed Principle) : 개방 폐쇄의 원칙, 확장에는 열려있고 변경에는 닫혀있다.
    - LSP(Liskov Substitution Principle) : 리스코프 치환의 원칙, 하위 클래스는 인터페이스의 규약을 지켜야한다.
    - ISP(Interface Segregation Principle) : 인터페이스 분리의 원칙, 특정 클라이언트와의 인터페이스가 낫다.(세분화가 낫다)
    - DIP(Dependency Inversion Principle) : 의존관계역전의 원칙, 구현클래스에 의존하는 것이 아닌 인터페이스에 의존해야한다. 

***
# 2. 스프링 핵심 원리 토이프로젝트
### 2.1. 사용 버전
    springframework.boot version '2.7.17'
    java 11

### 2.2. 주문 도메인 클래스 다이어그램
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/9f75b142-e848-4911-abd5-7d7ddc14fd3e)

### 2.3. 순수 자바 코드 작성 시
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/b1991a1d-a7fd-4346-ad24-6470ca0614ac)
```java
public class OrderServiceImpl implements OrderService {
    // private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
}
```
* 위 코드 때문에 구현체에 의존하는 일이 발생한다(DIP 위반)
* 코드 확장시 클라이언트 코드에 영향을 준다(OCP 위반)

### 2.4.1 AppConfig의 등장
```java
public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
    public OrderService orderService() {
        return new OrderServiceImpl(
            new MemoryMemberRepository(),
            //new FixDiscountPolicy()
            new RateDiscountPolicy()
        );
    }
}
```
* 구현 객체를 생성하고 연결하는 책임을 갖는 새로운 클래스를 만들어 문제를 해결한다.(DIP, OCP 해결)
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/526eefd2-2577-448c-a607-028135bcfbbc)
