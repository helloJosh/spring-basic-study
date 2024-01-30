# 스프링 핵심 원리 기본편 내용 정리
> 김영한 강사님의 스프링 핵심원리 강의 내용을 정리한 문서입니다.

## 1. 객체지향형 설계와 스프링
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
