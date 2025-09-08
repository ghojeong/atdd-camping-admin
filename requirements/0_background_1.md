# 인수 테스트와 Cucumber

[2주차 강의 자료] 인수 테스트 설계 전략

## 1. 들어가며: 왜 테스트는 금방 어수선해지는가?

개발자는 본능적으로 "어떻게(How)"를 먼저 적습니다. 로그인·데이터 준비·호출·검증이 한 메소드에 뒤섞이며, 테스트는 길어지고 읽기 어려워집니다. 이번 주는 테스트를 한 편의 연극처럼 연출하는 법과, 외부에서
시스템을 두드리는 인수 테스트로 "가볍게, 빠르게" 시작하는 법을 익힙니다.

## (참고) ATDD와 TDD

*
* ATDD: 인수 조건(도메인 언어)으로 실패하는 시나리오를 먼저 작성하고, 기능이 그 기준을 충족하도록 구현/리팩터링한다.
* TDD: 내부 설계 품질을 높이는 단위/구성요소 수준의 빠른 루프다. ATDD와 상호 보완한다.
* 흐름: 요구사항 → 인수 조건(Gherkin) → 실패하는 인수 테스트(red) → 구현(green) → 개선(refactor).

## 2. 인수 조건 작성 원칙

* 비즈니스 언어(UL) 사용: 시스템 내부 용어 대신 사용자/PO(비개발자)가 쓰는 단어.
* 한 시나리오, 한 이유: 핵심 사건과 검증만 남기고 배경/기술 요소는 분리.
* 독립성과 최소 상태: 시나리오 간 공유 상태를 줄이고, 필요한 데이터만 준비.
* 명확한 When/Then: 행동(When)은 단수, 결과(Then)는 관찰 가능한 사실.

## 3. 인수 테스트와 Cucumber 한눈에 보기

* 인수 테스트 환경: 애플리케이션은 외부 프로세스로 구동, 테스트는 HTTP로만 상호작용합니다. 내부 구현 변화에 둔감하고 환경 독립성이 높습니다.
* Cucumber의 핵심
    * Gherkin(사람이 읽는 명세) ↔ Step Definitions(실행 코드) 연결
    * Runner(JUnit Platform Suite), GLUE(스텝 패키지), features/ 디렉터리

## 4. Gherkin의 3단계 추상화 모델

"인수 테스트에서 검증하고자 하는 것에 집중"한다는 것을 연극에 비유를 해보자면

| 연극의 요소 | Gherkin 요소 | 역할                     |
|--------|------------|------------------------|
| 스포트라이트 | Scenario   | 주인공과 핵심 사건(What)에만 집중  |
| 무대 설정  | Background | 여러 시나리오가 공유하는 비즈니스 전제  |
| 백스테이지  | Hooks 보이지  | 않는 기술 준비/정리(데이터 초기화 등) |

### 나쁜 예: 모든 것을 시나리오에 노출

```feature
Scenario: 재고 추가
    Given 관리자가 로그인했다
    And "캠핑 의자"가 등록되어 있고 재고가 10개다
    When 재고를 5개 추가하면
    Then 재고는 15개가 된다
```

### 좋은 예: 공통/기술 요소를 숨기고 핵심만 남김

```feature
Background:
    Given 관리자가 로그인했다

Scenario: 등록된 상품의 재고를 추가한다
    Given 재고가 10개인 "캠핑 의자" 상품이 등록되어 있다
    When 재고를 5개 추가하면
    Then 재고는 15개가 된다
```

원칙 체크리스트:

* 이 스텝이 없으면 시나리오가 의미가 사라지는가? → Scenario
* 다른 시나리오와 공유되는 비즈니스 전제인가? → Background
* 비즈니스와 무관한 기술 준비인가? → Hooks

## 5. 테스트 인프라 구성 가이드

RequestSpec 공통화:

```java
String baseUrl = System.getProperty("test.baseUrl", System.getenv("TEST_BASE_URL"));
if(baseUrl ==null||baseUrl.

isBlank())baseUrl ="http://localhost:8080";
RequestSpecification spec = new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setAccept(ContentType.JSON)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL)
        .build();
```

Hooks:

```java

@Before
public void beforeScenario() {CommonContext.setRequestSpec(RequestSpecFactory.create());}
```

JWT 로그인 Step:

```java
Response res = given().spec(spec)
        .body(Map.of("username", "admin", "password", "admin123"))
        .post("/auth/login");
String token = res.then().extract().jsonPath().getString("token");
if(token ==null)token =res.

getCookie("AUTH_TOKEN");
```

디버깅 체크리스트:

* 302: Accept 헤더를 JSON으로 설정(HTML 리다이렉트 회피)
* 401: 자격증명/토큰 주입 확인, application.yml의 admin 계정 확인
* 연결 실패: BASE URL/포트 확인(TEST_BASE_URL, test.baseUrl)

## 6. Step 재사용과 구조화

파라미터화된 Then으로 재사용 극대화:

```java

@Then("응답 상태코드는 {int}이다")
public void assertStatus(int code) {lastResponse.then().statusCode(code);}

@Then("응답 본문의 {string}는 {string}이다")
public void assertBody(String path, String expected) {
    lastResponse.then().body(path, equalTo(expected));
}
```

공통 헬퍼 예시: AuthHelper(로그인/헤더 주입), RequestSpecFactory(BASE URL/헤더), DataFactory(동적 테스트 데이터)

정적 컨텍스트 사용 시 병렬 실행 비권장(충돌 방지)
