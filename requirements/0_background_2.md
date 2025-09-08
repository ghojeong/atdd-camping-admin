# Cucumber 환경 구성

Gherkin 명세 기반 인수 테스트

## 🎯 목표

인수 테스트로 “명세 → 구현 → 리팩터링”을 단계별로 체득한다. 취소 성공과 재예약 가능성을 도메인 결과로 검증하고, 중복은 Background/Hook로 점진적 분리한다.

## Part 1: 환경 설정

### 1-1. 의존성 추가 (build.gradle)

Cucumber와 RestAssured를 추가한다. (cucumber-spring 불필요)

cucumber-spring는 테스트 동작 시 스프링 애플리케이션을 함께 띄우는게 아닌 경우 필요 없음

```java
// atdd-camping-admin/build.gradle
dependencies {
    testImplementation 'io.cucumber:cucumber-java:7.14.0'
    testImplementation 'io.cucumber:cucumber-junit-platform-engine:7.14.0'
}
```

### 1-2. 테스트 실행기(Test Runner)

JUnit Platform 러너로 Cucumber를 실행한다. 서버는 외부에서 별도로 띄운다.

```java
// src/test/java/com/camping/CucumberTestRunner.java
@Suite
@SelectClasspathResource("features")
public class CucumberTestRunner {}
```

## Part 2: 시나리오 제시 -> 구현 (반복)

### 2-1. 인수 조건 1 제시 (취소 후 재예약 가능)

```feature
# src/test/resources/features/selected-feature.feature
Feature: 예약 취소를 관리자가 수행한다
    Scenario: 사용자가 예약한 건을 관리자가 취소하면 성공하고 재예약 가능하다
    Given 사용자가 예약을 했다
    When 관리자가 예약을 취소했다
    Then 예약은 취소 상태다
    And 해당 자원은 다시 예약 가능하다
```

### 2-2. 인수 조건 1 구현

```java
// src/test/java/com/camping/admin/steps/ReservationSteps.java
@When("관리자가 예약을 취소했다")
public void adminCancelledReservation() {
    lastResponse = given().spec(CommonContext.getRequestSpec())
            .header("Authorization", "Bearer " + CommonContext.getAdminToken())
            .body(Map.of("status", "CANCELLED"))
            .patch("/admin/reservations/" + reservationId + "/status");
}

@Then("예약은 취소 상태다")
public void assertReservationCancelled() {
    lastResponse.then().body("status", equalTo("CANCELLED"));
}

@Then("해당 자원은 다시 예약 가능하다")
public void assertResourceRebookable() {
    String siteNumber = lastResponse.then().extract().jsonPath().getString("campsite.siteNumber");
    String date = lastResponse.then().extract().jsonPath().getString("startDate");
    given().spec(RequestSpecFactory.create())
            .get("/api/sites/" + siteNumber + "/availability?date=" + date)
            .then().statusCode(200).body("available", equalTo(true));
}
```

### 2-3. 인수 조건 2 제시 (이미 취소된 예약 재취소)

```feature
Scenario: 이미 취소된 예약을 다시 취소 시도하면 정책에 맞게 동작한다
    Given 사용자가 예약을 했다
    And 관리자가 해당 예약을 취소했다
    When 관리자가 동일 예약을 다시 취소했다
    Then 시스템 정책에 맞는 결과가 반환된다(멱등/오류)
```

### 2-4. 인수 조건 2 구현

```java
// src/test/java/com/camping/admin/steps/ReservationSteps.java
@When("관리자가 동일 예약을 다시 취소했다")
public void adminCancelledReservationAgain() {
    int reservationId = lastResponse.then().extract().path("id");
    given().spec(CommonContext.getRequestSpec())
            .header("Authorization", "Bearer " + CommonContext.getAdminToken())
            .body(Map.of("status", "CANCELLED"))
            .patch("/admin/reservations/" + reservationId + "/status").then();
}
```

## Part 3: 중복 제거 (Background/Hook/재사용 Step)

### 3-1. Background로 공통 Given 분리

```feature
Background:
    Given 사용자가 예약을 했다

Scenario: 사용자가 예약한 건을 관리자가 취소하면 성공하고 재예약 가능하다
    When 관리자가 예약 1을 취소했다
    Then 예약은 취소 상태다
    And 해당 자원은 다시 예약 가능하다

Scenario: 이미 취소된 예약을 다시 취소 시도하면 정책에 맞게 동작한다
    And 관리자가 해당 예약을 취소했다
    When 관리자가 동일 예약을 다시 취소했다
    Then 시스템 정책에 맞는 결과가 반환된다(멱등/오류)
```

### 3-2. Hook로 공통 기술 설정 숨기기

```java
// src/test/java/com/camping/admin/steps/Hooks.java
@Before
public void beforeScenario() {CommonContext.setRequestSpec(RequestSpecFactory.create());}

// src/test/java/com/camping/admin/steps/TokenBootstrap.java
public class TokenBootstrap {
    @BeforeAll
    public static void initTokens() {
        RequestSpecification spec = RequestSpecFactory.create();
        String adminToken = given().spec(spec)
                .body(Map.of("username", "admin", "password", "admin123"))
                .post("/auth/login").then().extract().cookie("AUTH_TOKEN");
        String userToken = given().spec(spec)
                .body(Map.of("username", "user", "password", "user123"))
                .post("/auth/login").then().extract().cookie("AUTH_TOKEN");
        CommonContext.setAdminToken(adminToken);
        CommonContext.setUserToken(userToken);
    }
}
```

### 3-3. 재사용 Step 추가 예시

```java

@Then("응답 본문의 {string}는 {string}이다")
public void assertBodyEquals(String path, String expected) {lastResponse.then().body(path, equalTo(expected));}

@When("{word} 토큰으로 호출한다")
public void callWithActorToken(String actor) {
    String token = actor.equalsIgnoreCase("관리자") ? CommonContext.getAdminToken() : CommonContext.getUserToken();
    requestSpec = given().spec(CommonContext.getRequestSpec()).header("Authorization", "Bearer " + token);
}
```

## Part 4: 실행 방법/디버깅 체크리스트

```
# 서버 기동 후(기본 8080)
TEST_BASE_URL=http://localhost:8080 ./gradlew :atdd-camping-admin:test
```

* 302면 Accept 헤더를 JSON으로 설정해 HTML 리다이렉트 회피
* 401이면 토큰 부트스트랩/주입 확인
* BASE URL/포트(TEST_BASE_URL) 확인

## 인수 테스트의 가치

* 살아있는 문서: 명세와 코드가 함께 진화하며 현재 시스템 동작을 설명
* 자동화된 안전벨트: 변경으로 인한 회귀를 빠르게 감지
* 공유 언어: 모든 역할이 Gherkin을 중심으로 합의 형성
