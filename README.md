# ToDoAPI
(주) 클러쉬 백엔드 개발자 과제 - 할일 (ToDo) API

## 1. 앱 설명
이 API는 할 일 목록을 관리하는 API입니다. 사용자는 다음과 같은 기능을 이용할 수 있습니다:
- 기본적인 CRUD 작업 (생성, 조회, 수정, 삭제)
- 완료된 작업과 미완료된 작업 필터링
- 중요도에 따른 정렬
- 기한이 다가오는 작업 확인
- 완료 비율 계산 통계 기능

## 2. 소스 빌드 및 실행 방법

### 2.1. 필수 소프트웨어
- **Java 17 이상**
- **Gradle 7.0 이상**
- **MySQL**

### 2.2. 데이터베이스 설정
1. MySQL에서 `clush_todo`라는 이름의 데이터베이스를 생성합니다.
2. `src/main/resources/application.properties` 파일을 열어 데이터베이스 연결 정보를 설정합니다
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/clush_todo
    spring.datasource.username=root
    spring.datasource.password=korea123
    ```

### 2.3. Gradle 빌드 및 실행
1. Gradle 프로젝트 홈 디렉토리에서 `./gradlew build` 명령어를 실행하여 프로젝트를 빌드합니다.
2. `java -jar build/libs/ToDoList-0.0.1-SNAPSHOT.jar` 명령어를 실행하여 JAR 파일을 실행합니다.


<h3>3. 주력으로 사용한 컴포넌트</h3>
<h4>1. Spring Boot</h4>
Spring Boot는 @RestController와 @Service 등의 어노테이션을 사용하여 어플리케이션 구조를 간단하게 하고, RESTful API를 손쉽게 구현할 수 있도록 도와줍니다.<br>
Spring Boot는 @RestController, @Service 어노테이션으로 REST API와 서비스 로직을 자동으로 구성할 수 있도록 해줍니다. 예시로, 제가 구현한 ToDoController 클래스는 /api 엔드포인트에 대한 HTTP 요청을 처리합니다.<br>
Spring Boot는 Tomcat 서버를 내장하고 있으므로, 별도의 서버 설정을 하지 않아도 어플리케이션 실행이 가능합니다.<br>
NoContentException 등의 예외가 발생하면 Spring Boot가 예외 처리를 하여 적적한 HTTP 상태 코드와 메세지를 반환할 수 있습니다.

<h4>2. MySQL</h4>
MySQL은 어플리케이션 데이터 저장소로 사용되며 application.properties파일에서 DB 연결을 설정합니다.<br>
제가 구현한 ToDoRepository는 MySQL과 상호 작용하여 ToDo 항목을 저장하고 조회, 수정, 삭제 등을 처리합니다. 이는 데이터의 일관성과 무결성을 보장합니다.<br>
ToDoRepository에서 제공하는 다양한 메서드를 통해서 복잡한 데이터 검색과 조작이 가능합니다.<br>
향후 멤버 로그인 기능이나 팀별 할일 공유 등 다른 기능의 확장성을 고려하여 RDMS인 MySQL을 사용하였습니다.

<h4>3. Gradle</h4>
Gradle은 프로젝트 빌드 및 의존성 관리를 자동화 합니다. build.gradle파일에서 빌드 스크립트를 정의할 수 있습니다.<br>
Gradle을 통해서 jpa, starter-web 등 필요 라이브러리를 관리할 수 있습니다.<br>
Gradle은 프로젝트를 컴파일하거나 테스트 패키징하는 빌드 프로세스를 자동화합니다.

<h4>4. JPA</h4>
JPA는 Java객체를 관계형 DB의 테이블에 매핑하고 DB의 CRUD 작업을 객체 지향적으로 처리할 수 있게 해주는 API입니다. JPA는 DB와의 상호작용을 간소화하여 SQL 쿼리 대신에 JPQL을 사용해서 DB작업을 수행할 수 있습니다.<br>
JPA를 사용하면 자바 객체를 DB 테이블과 매핑하여 객체 지향적으로 DB 작업 수행이 가능합니다. 예를 들어, ToDoRepository 인터페이스는 ToDo 엔티티의 CRUD 작업을 자동으로 처리해줍니다.<br>
JPA는 일일히 query문을 정의해주지 않아도, 메서드 이름을 기반으로 하여 쿼리를 자동으로 생성할 수 있습니다. 예를 들어, findByCompleted(boolean completed)는 completed 필드를 특정 값으로 하는 모든 ToDo 객체를 찾는 쿼리를 자동으로 생성합니다.<br>
JPA는 JPQL을 사용해 DB 쿼리를 객체 지향적으로 작성할 수 있습니다. JPQL은 DB 테이블 대신 자바 객체를 대상으로 합니다.<br>
JPA는 DB 트랜잭션을 자동으로 관리합니다. 이는 데이터의 일관성과 무결성을 보장하는 역할을 합니다.<br>
JPA는 DB와의 상호 작용을 객체 지향적으로 처리하여 코드의 가독성을 높이고, 개발 생산성을 향상시킬 수 있습니다.

<h3>4. API 명세</h3>
