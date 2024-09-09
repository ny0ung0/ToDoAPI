# ToDoAPI
(주) 클러쉬 백엔드 개발자 과제 - 할일 (ToDo) API

## 1. 앱 설명
이 API는 할 일 목록을 관리하는 API입니다. 사용자는 다음과 같은 기능을 이용할 수 있습니다
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
2. `src/main/resources/application.properties` 파일을 열어 아래 예시와 같이 본인 MySQL 데이터베이스 연결 정보를 설정합니다
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/clush_todo
    spring.datasource.username=root
    spring.datasource.password=korea123
    ```

### 2.3. Gradle 빌드 및 실행
1. 프로젝트 홈 디렉토리에서 `./gradlew build` 명령어를 실행하여 프로젝트를 빌드합니다.
2. `java -jar build/libs/ToDoList-1.0.0-SNAPSHOT.jar` 또는 `./gradlew bootRun` 명령어를 통해서 빌드 프로젝트를 실행합니다.

## 3. 주력으로 사용한 컴포넌트 설명 및 사용 이유

### Spring Boot

- **간편한 API 개발**: `@RestController`와 `@Service` 어노테이션을 통해 어플리케이션 구조를 간단하게 설정할 수 있습니다.
- **자동 구성**: RESTful API와 서비스 로직을 자동으로 구성합니다.
- **내장 서버**: Tomcat 서버를 내장하여 별도의 서버 설정 없이 어플리케이션을 실행할 수 있습니다.
- **예외 처리**: NoContentException 등의 예외를 처리하여 적절한 HTTP 상태 코드와 메시지를 반환할 수 있습니다.

### MySQL

- **데이터 저장**: 어플리케이션 데이터 저장소로 사용되며, 데이터의 일관성과 무결성을 보장합니다.
- **복잡한 쿼리 처리**: ToDoRepository를 통해 복잡한 데이터 검색과 조작이 가능합니다.
- **확장성**: 향후 멤버 로그인 기능이나 팀별 할 일 공유 등 기능 확장을 고려하여 RDMS인 MySQL을 사용하였습니다.

### Gradle

- **빌드 자동화**: 프로젝트 빌드 및 의존성 관리를 자동화합니다.
- **의존성 관리**: `build.gradle` 파일에서 라이브러리 의존성을 관리할 수 있습니다.
- **프로세스 자동화**: 컴파일, 테스트, 패키징 등의 빌드 프로세스를 자동화합니다.

### JPA

- **객체-관계 매핑**: 자바 객체를 DB 테이블에 매핑하고 CRUD 작업을 객체 지향적으로 처리할 수 있습니다.
- **쿼리 자동 생성**: 메서드 이름을 기반으로 쿼리를 자동으로 생성합니다. 예를 들어, `long countByCompleted(boolean completed)` 메서드는 자동으로 완료된 할 일의 수를 계산하는 쿼리를 생성합니다.
- **JPQL**: 객체 지향적인 쿼리 언어인 JPQL을 사용하여 DB 쿼리를 작성합니다.
- **트랜잭션 관리**: DB 트랜잭션을 자동으로 관리하여 데이터의 일관성과 무결성을 보장합니다.

### Swagger (springdoc-openapi)

- **API 문서 자동화**: `@Operation`, `@Schema` 같은 어노테이션을 사용해 API의 엔드포인트, 요청, 응답 구조를 자동으로 문서화 할수 있습니다. 이는 개발자가 REST API를 더욱 정확하게 이해할 수 있게 하며, 팀원 간 의사소통을 원활하게 할 수 있습니다.
- **Swagger UI제공**: 자동 생성된 API문서를 시각적으로 제공하여 사용자가 API의 동작을 손쉽게 테스트해볼 수 있습니다.
- **예제 값 설정**: `@ExampleObject` 어노테이션을 사용해 API 요청 및 응답에서 사용할 예제 값을 정의하여, API 사용자가 어떤 데이터 형식으로 요청해야 하는지 쉽게 이해할 수 있습니다.

### ToDo Controller(Rest Controller)
- 컨트롤러는 클라이언트가 REST API를 통해 서버와 소통할 수 있도록 합니다. 각각의 메서드는 특정 HTTP 메서드(GET, POST, PUT, DELETE 등)와 매핑되어 API 엔드포인트를 간결하게 정의할 수 있습니다.
- 서비스 계층을 사용하여 컨트롤러에서는 비즈니스 로직을 처리하지 않고, 요청을 서비스로 전달해서 처리합니다. 이렇게 하면 코드의 가독성을 높일 수 있고 유지보수에도 용이합니다.

### ToDo Service
- 서비스는 실제로 요청을 처리하는 "뇌"라고 볼 수 있습니다. 여기서 비즈니스 로직, 즉 작업을 어떻게 처리할지에 대한 구체적인 내용을 정의합니다. 서비스 계층을 이용해서 복잡한 로직을 분리하고, 컨트롤러는 요청을 받아서 전달하는 역할만 하기 때문에 코드가 깔끔하고 관리하기 쉬워집니다.
- 서비스 계층을 이용하면 비즈니스 로직을 독립적으로 테스트할 수 있고, 로직을 변경하더라도 영향을 최소화할 수 있습니다.

### ToDoRepository
- Repository 인터페이스는 기본적인 CRUD메서드를 자동으로 제공하므로 개발자가 별도의 SQL 쿼리를 작성하지 않아도 DB 작업을 간단하게 처리할 수 있습니다.
- DB와 직접적인 상호작용을 코드에서 쉽게 처리할 수 있게 도와주며, 데이터에 액세스할 때 자바 객체(클래스)를 다루는 것처럼 직관적으로 처리할 수 있습니다. 또, 메서드 이름 기반으로 쿼리를 자동 생성해주는 기능을 통해서 복잡한 쿼리도 손쉽게 작성이 가능합니다.

### ToDo Entity
- Entity는 DB의 테이블과 자바 객체를 연결하는 역할을 합니다. DB의 정보를 코드에서 객체를 다루게 해줍니다. Entity를 이용하면 할 일 항목을 코드에서 객체처럼 다루기 때문에 DB작업이 직관적이고 편리해집니다.

### Exception Handling(예외 처리)
- 예외 처리는 요청 처리 중에 문제가 발생한 경우, 이를 처리하고 사용자에게 적절한 메세지를 보여주는 역할을 합니다. 예를 들어, 할 일이 없는데 삭제 요청을 보냈을 때 "해당 항목을 찾을 수 없습니다"라는 메세지를 반환합니다.

## 4. API 명세
build 실행 후 클릭해주세요.
<a href="http://localhost:8080/clush-backend.html">Swagger UI로 확인하기기</a>

## 5. DB스키마
```sql
CREATE TABLE `to_do` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `completed` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duedate` date NOT NULL,
  `importance` enum('high','low','medium') DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);
```
<strong>해당 API는 JPA를 사용하여 구현하였으므로 별도로 MySQL에 테이블 생성 쿼리문을 작성할 필요는 없습니다.</strong>
<hr>

### 과제를 봐주셔서 감사합니다 !
