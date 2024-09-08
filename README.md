# ToDoAPI
(주) 클러쉬 백엔드 개발자 과제 - 할일 (ToDo) API

<h3>1. 앱 설명</h3>
이 API는 할 일 목록을 관리하는 API입니다.
사용자는 할 일에 대한 기본적 CRUD를 할 수 있으며, 완료된 작업과 미완료된 작업 필터링, 중요도에 따른 정렬을 사용할 수 있습니다.
또한, 기한이 다가오는 작업을 확인하고 완료 비율을 계산하는 통계 기능을 제공하고 있습니다.

<h3>2. 소스 빌드 및 실행 방법</h3>
<h4>2.1. 필수 소프트웨어</h4>
<li>Java 17 이상</li>
<li>Gradle 7.0이상</li>
<li>MySQL</li>
<h4>2.2. 데이터베이스 설정</h4>
1. MySQL에서 <strong>clush_todo</strong>라는 이름의 데이터베이스를 생성합니다.
2. src/main/resources/application.properties 파일을 열어 데이터베이스 연결 정보를 설정합니다.
2-1 ex) spring.datasource.url=jdbc:mysql://localhost:3306/clush_todo
        spring.datasource.username=root
        spring.datasource.password=korea123

<h4>2.3 Gradle 빌드 및 실행</h4>
1. Gradle 프로젝트 홈 디렉토리에서 <strong>./gradlew build</strong> 명령어를 실행하여 프로젝트를 빌드해줍니다.
2. <strong>java -jar build/libs/ToDoList-0.0.1-SNAPSHOT.jar</strong> 명령어를 실행하여 JAR파일을 실행 합니다.

<h3>주력으로 사용한 컴포넌트</h3>
<li></li>

<h3>API 명세</h3>
