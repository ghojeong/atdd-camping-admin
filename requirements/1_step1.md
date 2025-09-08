# 1단계 - Cucumber 적용하기

## 요구사항

### 1. 기능 선택

* 아래 "기능 후보(실제 엔드포인트)"에서 1가지를 선택하세요.
* 리스크를 판단하여 해당 기능을 첫 보호 대상으로 확정하세요.

#### 기능 후보:

* 예약 상태 변경: PATCH /admin/reservations/{id}/status (본문: { "status": "..." })
* 예약 목록 조회: GET /admin/reservations
* 상품 생성: POST /admin/products
* 캠프사이트 생성: POST /admin/campsites
* 대여 생성: POST /admin/rentals

### 2. Cucumber 테스트 환경 구성

* 테스트를 진행할 애플리케이션은 별도 프로세스로 기동하고(기본 http://localhost:8080), 테스트는 RestAssured를 이용하여 HTTP로 호출합니다.
* 이 때 필요한 환경을 구축한합니다.
    * TestRunnerClass

### 3. Gherkin 시나리오 작성(선택 기능의 Happy Path)

* `features/` 하위에 Happy Path를 1개 작성합니다.

### 4. 인수 테스트 구현

* Step 구현 위치: src/test/java/com/camping/admin/steps

### 5. 추가 구현

* 필요 시 hook, contest, 각종 팩토리 등 테스트 도구로 필요한 로직을 구현합니다.

## 제출 산출물

### 1. feature 파일

```feature
# src/test/resources/features/reservation.feature

Feature: 예약 취소
Scenario: 사용자가 예약한 건을 관리자가 취소하면 성공하고 재예약 가능하다
Given 사용자가 예약을 했다
When 관리자가 예약 1을 취소했다
Then 예약은 취소 상태다
And 해당 자원은 다시 예약 가능하다
```

### 2. Steps 예시

```java
   // src/test/java/com/camping/admin/steps/ReservationSteps.java
@When("관리자가 예약 {int}을 취소했다")
public void adminCancelledReservation(int reservationId) {
    lastResponse = given().spec(CommonContext.getRequestSpec())
            .header("Authorization", "Bearer " + CommonContext.getAdminToken())
            .body(Map.of("status", "CANCELLED"))
            .patch("/admin/reservations/" + reservationId + "/status");
}
```

## 체크포인트

* [] 선택한 기능의 Happy Path를 비즈니스 용어로 명확히 표현했는가?
* [] Cucumber 러너/피처/스텝/훅/요청 스펙이 정상 동작하는가?
* [] JWT 로그인 후 토큰을 사용해 인증이 통과되는가?(Accept: JSON)
