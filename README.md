# 🏕️ 초록 캠핑장 관리자 시스템

## 📖 프로젝트 소개
초록 캠핑장 체인의 관리자를 위한 통합 관리 시스템입니다. 예약 관리, 사이트 관리, 매출 통계 등의 기능을 제공합니다.

> ⚠️ **주의**: 이 프로젝트는 ATDD 학습을 위한 레거시 시스템 예제입니다. 의도적으로 복잡한 구조와 중복 코드가 포함되어 있습니다.

## 🚀 시작하기

### 필수 요구사항
- Java 17 이상
- Gradle 7.0 이상

### 실행 방법
```bash
# 프로젝트 클론
git clone [repository-url]
cd camping-admin-system

# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

### 접속 정보
- **애플리케이션**: http://localhost:8081
- **H2 콘솔**: http://localhost:8081/h2-console
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: (빈 값)

## 👤 테스트 계정

| 역할 | 이메일 | 비밀번호 | 권한 |
|------|--------|----------|------|
| 관리자 | admin | admin123 | 모든 캠핑장 관리 |

## 🏗️ 시스템 구조

### 주요 기능
1. 대시 보드
2. 상품 관리
3. 사이트 관리
4. 예약 관리
5. 매출 조회