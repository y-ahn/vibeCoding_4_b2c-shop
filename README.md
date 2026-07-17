# 바이브코딩 Vol.4 — B2C 이커머스 플랫폼

> Spring Boot + JPA 실전 설계 패턴 — PART 8 실전 프로젝트

[![Java](https://img.shields.io/badge/Java-21-blue)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-blue)](https://gradle.org)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)](https://www.mysql.com)
[![Redis](https://img.shields.io/badge/Redis-7.0-red)](https://redis.io)

---

## 📌 프로젝트 소개

바이브코딩 Vol.4 PART 1~8에서 배운 **설계 패턴이 모두 적용된 B2C 이커머스 플랫폼**입니다.

| 적용 패턴 | 위치 |
|-----------|------|
| DDD (Aggregate, Value Object, Domain Event) | `domain/order/entity/` |
| 헥사고날 아키텍처 (Port & Adapter) | `domain/*/port/`, `domain/*/adapter/` |
| CQRS (쓰기:JPA / 읽기:QueryDSL DTO) | `OrderJpaRepository`, `ProductJpaRepository` |
| 이벤트 기반 설계 (Outbox 패턴) | `KafkaEventPublisher`, `OutboxEventEntity` |
| 전략 패턴 (결제 수단) | `PaymentStrategy`, `CardPaymentStrategy` |
| 상태 패턴 (주문 상태머신) | `Order.pay()`, `Order.cancel()`, `Order.ship()` |
| Soft Delete | `Product` (`@SQLDelete` + `@Where`) |
| Audit 자동화 | `BaseEntity` (`@CreatedDate` 등) |

---

## 🏗️ 헥사고날 아키텍처 패키지 구조

```
com.vibecoding.shop/
├── EcommerceApplication.java
├── common/
│   ├── exception/          # 예외 계층 + GlobalExceptionHandler
│   ├── response/           # ApiResponse<T> 공통 응답 포맷
│   └── util/               # BaseEntity (Audit 자동화)
├── infrastructure/
│   └── config/             # SecurityConfig, QueryDslConfig
└── domain/
    ├── order/              # 주문 도메인 (핵심)
    │   ├── entity/         # Order(Aggregate), OrderLine, Money, ShippingInfo
    │   ├── port/in/        # UseCase 인터페이스 (PlaceOrder, Pay, Cancel)
    │   ├── port/out/       # Repository, ProductStock, EventPublisher 인터페이스
    │   ├── application/    # UseCase 구현체 (도메인 + Port만 의존)
    │   ├── adapter/in/     # REST Controller (Inbound Adapter)
    │   └── adapter/out/    # JPA, Kafka (Outbound Adapter)
    ├── product/            # 상품 도메인 (CQRS + Soft Delete)
    ├── member/             # 회원 도메인 (Redis 캐싱)
    ├── payment/            # 결제 도메인 (전략 패턴)
    └── notification/       # 알림 도메인 (이벤트 리스너)
```

---

## 🚀 실행 방법

### STEP 1 — JDK 21 설치 확인
```bash
java -version   # 21 이상 확인
```
> JDK 21 다운로드: https://adoptium.net

### STEP 2 — Docker Desktop 실행 후 MySQL & Redis 시작
```bash
docker compose up -d mysql redis

# MySQL 준비 완료 확인 (30초 대기 후)
docker logs shop-mysql 2>&1 | tail -3
# "ready for connections" 메시지 확인
```

### STEP 3 — IntelliJ 설정
```
Settings → Build, Execution, Deployment
  → Compiler → Annotation Processors
  → Enable annotation processing ✅ 체크 (Lombok 필수)
```

### STEP 4 — QueryDSL Q클래스 생성
```bash
./gradlew compileJava
```

### STEP 5 — 앱 실행
```bash
./gradlew bootRun --args='--spring.profiles.active=dev' -x test
```

### STEP 6 — 실행 확인
```
http://localhost:8080/actuator/health   → {"status":"UP"}
http://localhost:8080/swagger-ui.html  → API 문서
```

---

## 🧪 API 테스트 순서 (Swagger)

```
STEP 1: POST /api/v1/auth/signup       → 회원가입
STEP 2: POST /api/v1/auth/login        → 로그인 (토큰 받기)
STEP 3: Swagger Authorize 🔒           → Bearer {token} 등록
STEP 4: GET  /api/v1/products          → 상품 목록 조회
STEP 5: POST /api/v1/orders            → 주문 생성
STEP 6: POST /api/v1/payments          → 결제 (CARD/KAKAO_PAY)
STEP 7: GET  /api/v1/orders/me         → 내 주문 목록 (CQRS)
STEP 8: DELETE /api/v1/orders/{id}     → 주문 취소 (보상 트랜잭션)
```

---

## ❗ 자주 발생하는 에러 & 해결법

| 에러 | 해결 |
|------|------|
| `Communications link failure` | `docker compose up -d mysql redis` |
| `cannot find symbol QOrder` | `./gradlew compileJava` 먼저 실행 |
| `Lombok getter 없음` | IntelliJ Annotation Processors 활성화 |
| `sourceCompatibility 에러` | gradle-wrapper.properties Gradle 8.5 확인 |

---

Published by **Rentify** | 안영준 · 디지털 콘텐츠 기획자 | IT 스페셜리스트
