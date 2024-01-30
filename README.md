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
# 2. 예제 토이프로젝트
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

### 2.4. AppConfig의 등장
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

***
# 3. 스프링 컨테이너와 스프링 빈
### 3.1 스프링 컨테이너 생성
```java
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/b2d637fd-e0a7-422c-8503-93597fe51c2a)
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/24121c7b-7465-4d2c-97dd-d35c5affbb46)

### 3.2 컨테이너에 등록된 빈 조회
* ```ac.getBeanDefinitionNames()``` : 스프링에 등록된 모든 빈 이름 조회
* ```ac.getBean()``` : 매개변수 빈이름, 타입 넣어서 빈 찾기
* ```beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION``` : 일반적으로 사용자가 정의한 빈
* ```beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE``` : 스프링이 내부에서 사용하는 빈
* ```ac.getBeansOfType()``` : 해당 타입의 모든 빈 조회

### 3.3 BeanFactory와 ApplicationContext
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/87d8cf6a-cb07-4b1b-8de6-c6826157a84f)
#### 3.3.1. BeanFactory
    - 스프링 컨테이너의 최상위 인터페이스
    - 스프링 빈을 관리하고 조회하는 역할
    - getBean() 와 같은 조회 기능 제공
#### 3.3.2. ApplicationContext
    - BeanFactory 말고도 메시지소스 활용한 국제화 기능, 환경변수, 애플리케이션 이벤트, 리소스 조회 기능과 같은 인터페이스를 상속 받는다
    - BeanFactory 기능을 모드 상속
#### 3.3.3. BeanDefinition
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/92653932-d558-435f-a322-f29e178fa968)
* `AnnotationConfigApplicationContext` 는 `AnnotatedBeanDefinitionReader` 를 사용해서 `AppConfig.class` 를 읽고 `BeanDefinition` 을 생성한다.
* `GenericXmlApplicationContext` 는 `XmlBeanDefinitionReader` 를 사용해서 `appConfig.xml` 설정 정보를 읽고 `BeanDefinition` 을 생성한다.
* 새로운 형식의 설정 정보가 추가되면, XxxBeanDefinitionReader를 만들어서 `BeanDefinition` 을 생성하면 된다.

***
# 4. 싱글톤 컨테이너
### 4.1 스프링 싱글톤 컨테이너
* 스프링 컨테이너는 기본적으로 객체 인스턴스를 싱글톤으로 관리한다.
  + 컨테이너는 객체를 하나만 생성해서 관리한다.
* 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라고 한다.
* 스프링 컨테이너의 이러한 기능덕분에 싱글턴 패턴의 단점을 해결하면서 객체를 싱글톤으로 유지한다.
  + 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않는다
  + DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용한다.

### 4.2 싱글톤 방식의 주의점
    1. 무상태(stateless)로 설계해야 한다!
    2. 특정 클라이언트에 의존적인 필드가 있으면 안된다.
    3. 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다!
    4. 가급적 읽기만 가능해야 한다.
    5. 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
    6. 스프링 빈의 필드에 공유 값을 설정하면 정말 큰 장애가 발생할 수 있다!
    
### 4.3 @Configuration과 싱글톤
* Bean으로 만들어진 객체는 중복되는 객체가 있어도 스프링 컨테이너가 알아서 중복을 제거해준다.

***
# 5. ComponentScan
### 5.1. @Configuration
```java
@Configuration
@ComponentScan(
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
    Configuration.class))
```
* AppConfig 클래스 파일에 위 Annotation을 추가하면 클래스마다 붙어있는 @Component을 자동으로 스캔하고 @Autowired를 찾아 의존관계를 자동으로 주입한다.

### 5.2. 탐색할 패키지의 시작 위치 지정
```java
@Configuration
@ComponentScan(
    basePackages = "hello.core",
}
```
* 위와 같이 위 패키지를 포함하여 하위 패키지를 탐색한다.
* 스프링에서는 패키지 위치를 지정하지 않고 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것을 권장한다.

### 5.3. 컴포넌트 스캔 기본 대상
* `@Component` : 컴포넌트 스캔에서 사용
* `@Controller` : 스프링 MVC 컨트롤러에서 사용
* `@Service` : 스프링 비즈니스 로직에서 사용
* `@Repository` : 스프링 데이터 접근 계층에서 사용
* `@Configuration` : 스프링 설정 정보에서 사용

### 5.4. 필터
```java
@Configuration
@ComponentScan(
    includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
)
```
* `includeFilters` : 컴포넌트 스캔 대상을 추가로 지정한다.
* `excludeFilters` : 컴포넌트 스캔에서 제외할 대상을 지정한다.
* includeFilter는 정말 사용할 일이 없고, excludeFilter는 가끔있다고한다.
> (필요할 일이 있으면 pdf 6.컴포넌트 스캔을 찾아보자)
* 하지만 스프링기본설정에 최대한 맞추어 사용하는 것을 권장한다.

### 5.5. 중복 등록과 충돌
* 자동빈 등록 vs 자동빈 등록 : `ConflictingBeanDefinitionException` 예외 발생
* 수동빈 등록 vs 자동빈 등록 : 수동 빈 등록이 우선권을 가진다.

***
# 6. 의존관계 자동 주입
### 6.1. 생성자 주입
```java
@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }    
}
```
* 이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법이다.
* 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
* **불변, 필수** 의존관계에 사용
* **중요! 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입 된다.** 물론 스프링 빈에만 해당한다.
