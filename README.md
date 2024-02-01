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

### 6.2. 수정자 주입(Setter 주입)
```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```
* Setter 함수에 필드에 바로 주입하는 방법
* 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점이 있다.
* DI 프레임워크가 없으면 아무것도 할 수 없다.
* 사용하지 않는 것을 권장한다.
* 애플리케이션의 실제 코드와 관계 없는 테스트 코드
* 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용

### 6.3. 일반 메서드 주입
```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```
* 일반 메서드를 통해 주입 받는다.
* 한번에 여러 필드를 주입받들 수 있다.
* 일반적으로 잘 사용하지 않는다.

### 6.4. 옵션처리
#### 6.4.1. @Autowired(required=false)
```java
@Autowired(required = false)
public void setNoBean1(Member member) {
    System.out.println("setNoBean1 = " + member);
}
```
* 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출이 안됨

#### 6.4.2. org.springframework.lang.@Nullable
```java
@Autowired
public void setNoBean2(@Nullable Member member) {
    System.out.println("setNoBean2 = " + member);
}
```
* 자동 주입할 대상이 없으면 null이 입력된다.

#### 6.4.3. Optional<>
```java
@Autowired(required = false)
public void setNoBean3(Optional<Member> member) {
    System.out.println("setNoBean3 = " + member);
}
```
* 자동 주입할 대상이 없으면 Optional.empty 호출

### 6.5. 최신 트랜드
```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
}
```
* 롬복 `@RequiredArgsConstructor`를 통해 생성자 코드 생략
* 생성자가 1개만 있으면 @Autowired도 생략되기 때문에 위 코드처럼 사용

***
# 7. 빈 생명주기 콜백
### 7.1. 스프링 빈의 이벤트 라이프사이클
* **스프링 컨테이너 생성** -> **스프링 빈 생성** -> **의존관계 주입** -> **초기화 콜백** -> **사용** -> **소멸전 콜백** -> **스프링 종료**
* **초기화 콜백**: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
* **소멸전 콜백**: 빈이 소멸되기 직전에 호출

### 7.2. 초기화 콜백, 소멸전 콜백 방법 3가지
#### 7.2.1. 인터페이스(InitializingBean, DisposableBean)
```java
package hello.core.lifecycle;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
public class NetworkClient implements InitializingBean, DisposableBean {
    // 중략
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");
    }
    @Override
    public void destroy() throws Exception {
        disConnect();
    }
}
```
* `InitializingBean` 은 `afterPropertiesSet()` 메서드로 초기화를 지원한다.
* `DisposableBean` 은 `destroy()` 메서드로 소멸을 지원한다.
* 이 인터페이스는 스프링 전용 인터페이스다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
* 초기화, 소멸 메서드의 이름을 변경할 수 없다.
* 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.

#### 7.2.2. 설정 정보에 초기화 메서드, 종료메서드 지정
```java
public class NetworkClient {
    // 중략
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}
```
```java
@Configuration
static class LifeCycleConfig {
    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://hello-spring.dev");
        return networkClient;
    }
}
```
* 메서드 이름을 자유롭게 줄 수 있다.
* 스프링 빈이 스프링 코드에 의존하지 않는다.
* 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.
* destroy method는 기본값이 추론으로 등록되어있어 close, shutdown이라는 이름의 메서드를 자동으로 호출해주는 기능이 있다.

#### 7.2.3. @PostConstruct, @PreDestory 애노테이션 지원
```java
package hello.core.lifecycle;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
public class NetworkClient {
    //w 중략
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}
```
* 최신 스프링에서 가장 권장하는 방법이다.
* 애노테이션 하나만 붙이면 되므로 매우 편리하다.
* 패키지를 잘 보면 `javax.annotation.PostConstruct` 이다. 스프링에 종속적인 기술이 아니라 JSR-250라는 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
* 컴포넌트 스캔과 잘 어울린다.
* 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기화, 종료 해야 하면 @Bean의 기능을 사용하자.

***
# 8. 빈 스코프
### 8.1. 빈스코프(빈이 존재할 수 있는 범위)
* **싱글톤**: 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다.
* **프로토타입**: 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프이다.
* **웹 관련 스코프**
    + **request**: 웹 요청이 들어오고 나갈때 까지 유지되는 스코프이다.
    + **session**: 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프이다.
    + **application**: 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프이다.
 
#### 8.1.1. 빈스코프 지정 방법
```java
@Scope("prototype")
@Component
public class HelloBean {}
```
* 컴포턴트 스캔 자동 등록
```java
@Scope("prototype")
@Bean
PrototypeBean HelloBean() {
    return new HelloBean();
}
```
* 수동 등록

### 8.2. 프로토타입 스코프
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/173cbbea-4eb9-49a9-afd6-025d88b8bc5a)
* 빈 요청마다 객체 생성
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/62cf6ff4-5521-4a59-8a5b-51c62d4c7a1d)
* 빈 반환 후 관리X
> 스프링컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화까지만 처리


### 8.3. 프로토타입 스코프를 싱글톤 빈과 함께 사용시 문제점
![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/f1e2203e-7069-4850-aaa6-b5c577b2b9db)
* clientBean` 은 싱글톤이므로, 보통 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관계 주입도 발생한다.
    + 1. `clientBean` 은 의존관계 자동 주입을 사용한다. 주입 시점에 스프링 컨테이너에 프로토타입 빈을 요청한다.
    + 2. 스프링 컨테이너는 프로토타입 빈을 생성해서 `clientBean` 에 반환한다. 프로토타입 빈의 count 필드 값은 0이다.
* 이제 `clientBean` 은 프로토타입 빈을 내부 필드에 보관한다. (정확히는 참조값을 보관한다.)


![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/0cda44a0-45b0-4339-9d0e-75e4eaa4e996)
* 클라이언트 A는 `clientBean` 을 스프링 컨테이너에 요청해서 받는다.싱글톤이므로 항상 같은 `clientBean`이 반환된다.
    + 3. 클라이언트 A는 `clientBean.logic()` 을 호출한다.
    + 4. `clientBean` 은 prototypeBean의 `addCount()` 를 호출해서 프로토타입 빈의 count를 증가한다. count값이 1이 된다.


![image](https://github.com/helloJosh/spring-basic-study/assets/37134368/669490d4-ce3c-44dd-8016-1bf1127a6124)
* 클라이언트 B는 `clientBean` 을 스프링 컨테이너에 요청해서 받는다.싱글톤이므로 항상 같은 `clientBean`이 반환된다.
* **여기서 중요한 점이 있는데, clientBean이 내부에 가지고 있는 프로토타입 빈은 이미 과거에 주입이 끝난 빈이다. 주입 시점에 스프링 컨테이너에 요청해서 프로토타입 빈이 새로 생성이 된 것이지, 사용 할 때마다 새로 생성되는 것이 아니다!**
    + 5. 클라이언트 B는 `clientBean.logic()` 을 호출한다.
    + 6. `clientBean` 은 prototypeBean의 `addCount()` 를 호출해서 프로토타입 빈의 count를 증가한다. 원래 count 값이 1이었으므로 2가 된다.

### 8.4. 프로토타입 스코프 - 싱글톤 빈과 함께 상시 Provider로 문제해결
#### 8.4.1 ObjectFactory, ObjectProvider
```java
@Autowired
private ObjectProvider<PrototypeBean> prototypeBeanProvider;
public int logic() {
    PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```
* 실행해보면 `prototypeBeanProvider.getObject()` 을 통해서 항상 새로운 프로토타입 빈이 생성되는 것
* 을 확인할 수 있다.
* `ObjectProvider` 의 `getObject()` 를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (**DL**)
* 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
* ObjectProvider` 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
* ObjectProvider: ObjectFactory 상속, 옵션, 스트림 처리등 편의 기능이 많고, 별도의 라이브러리 필요 없음,스프링에 의존
#### 8.4.2 JSR-330 Provider
> Spring Boot 3.0 미만 `javax.inject:javax.inject:1` 사용
> Spring Boot 3.0 이상 부터는 `jakarta.inject:jakarta.inject-api:2.0.1`를 사용
```java
@Autowired
private Provider<PrototypeBean> provider;

public int logic() {
    PrototypeBean prototypeBean = provider.get();
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```
* 실행해보면 `provider.get()` 을 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.
* `provider` 의 `get()` 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (**DL**)
* 자바 표준이고, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
* `Provider` 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
* `get()` 메서드 하나로 기능이 매우 단순하다.
* 별도의 라이브러리가 필요하다.
* 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.

### 8.5. 웹 스코프
#### 8.5.1 웹 스코프 특징
* 웹 스코프는 웹 환경에서만 동작한다.
* 웹 스코프는 프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리한다. 따라서 종료 메서드가 호출된다.

#### 8.5.2. 웹 스코프 종류
* **request:** HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리된다.
* **session:** HTTP Session과 동일한 생명주기를 가지는 스코프
* **application:** 서블릿 컨텍스트( `ServletContext` )와 동일한 생명주기를 가지는 스코프
* **websocket:** 웹 소켓과 동일한 생명주기를 가지는 스코프
