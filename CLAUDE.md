# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

이삿찜(Isajjim) 백엔드 — 사진 한 장으로 이사 견적을 산출하는 AI 이사 견적 서비스. 사용자가 집 사진을 업로드하면 외부 AI 서버가 가구를 분석해 트럭 개수 등 견적(3D 시뮬레이션 포함)을 산출한다. 이 Spring Boot 백엔드는 인증, 견적서 생명주기, 사용자-이사업체 채팅, 외부 AI/FastAPI 서버와의 연동을 담당한다.

스택: Spring Boot 3.5 (Java 17), Spring Data JPA, Spring Security + OAuth2(카카오/구글/네이버), JWT, Spring WebSocket(STOMP), Redisson, MySQL(로컬 런타임은 H2), Firebase Admin(Storage), AWS S3, Google Gemini, springdoc-openapi.

## 빌드 & 실행

```bash
./gradlew build               # 전체 빌드
./gradlew bootRun             # 로컬 실행 (.env 필요, 아래 참고)
./gradlew test                # 전체 테스트 실행
./gradlew test --tests "kr.co.isajjim.SomeTest"               # 단일 테스트 클래스
./gradlew test --tests "kr.co.isajjim.SomeTest.someMethod"    # 단일 테스트 메서드
```

- 로컬 민감 설정값은 `.env` 파일로 관리한다(`.env.example`을 복사해서 생성). IntelliJ에서는 EnvFile 플러그인으로 Run/Debug Configuration에 주입해야 한다 — Gradle/Spring이 자동으로 읽어오지 않는다.
- 기본 활성 프로파일은 `local`(`application.yml` → `application-local.yml`)이며, `application-dev.yml`은 배포된 dev/QA 환경에서 사용한다.
- Firebase 키 경로: `configs/firebaseKey.json` (`infra.firebase.key-path`에서 참조).

## CI/CD

`.github/workflows/ci.yml`, `cd.yml`, `cloudbuild.yaml`, `Dockerfile` 참고.

## 아키텍처

### 도메인형 패키지 구조

`kr.co.isajjim` 하위는 세 개의 최상위 패키지 그룹으로 구성된다:

- **`domains/<도메인>`** — 비즈니스 애그리거트별 패키지(`chat`, `estimate`, `furniture`, `image`, `refreshtoken`, `user`). 각 도메인은 동일한 내부 레이어 구조를 따른다:
  - `presentation/controller` — REST/WebSocket 컨트롤러. 얇게 유지하며 usecase로 바로 위임한다.
  - `presentation/api` — 컨트롤러가 `implements`하는 Swagger 문서화용 **인터페이스**. `@Operation`/`@ApiResponseExplanations` 등의 어노테이션은 모두 여기에 두고 컨트롤러에는 두지 않는다(컨트롤러 가독성 유지 목적). 엔드포인트를 추가/변경할 때는 `*Api` 인터페이스와 `*Controller` 구현체를 둘 다 수정해야 한다.
  - `application/usecase` — 사용자에게 노출되는 하나의 행위를 orchestrate. `domain/service`를 호출하고 `application/mapper`를 통해 엔티티를 응답 DTO로 변환한다. usecase가 다른 usecase를 호출하는 것은 지양한다.
  - `application/dto` (또는 도메인에 따라 `application` 바로 아래 `request`/`response`) — 요청/응답 record.
  - `application/mapper` — 엔티티 ↔ DTO 변환.
  - `domain/service` — 비즈니스 로직, `persistence/repository`에 의존.
  - `domain/constant` — enum (예: `RoomType`, `Floor`, `AIStatus`).
  - `domain/event` + `application/listener` — 횡단/비동기 후속 처리를 위한 Spring 애플리케이션 이벤트(예: `EstimateCreatedEvent` → `EstimateEventListener`).
  - `persistence/entity`, `persistence/repository` — DB 테이블과 1:1 매핑되는 JPA 엔티티/리포지토리.
  - `exception` — 존재하는 경우 도메인별 커스텀 예외.
- **`global`** — 횡단 관심사: `config/*`(security, websocket, s3, llm, firebase, swagger, async, cache, rest client, jackson), `security/*`(JWT + 카카오/구글/네이버 OAuth2 로그인, 필터, 핸들러), `exception`(전역 핸들러), `common`(`ApiResponse`, `ResponseCode`), `base/entity`(생성/수정 시각을 가진 `BaseEntity`), `sse`, `utils`.
- **`infra`** — 외부 시스템 연동: `infra/ai`(외부 AI/FastAPI 분석 서버 호출), `infra/s3`(AWS S3 업로드 usecase/service). domains와 동일한 usecase/service/mapper 하위 레이어 구조를 따른다.

전체 컨벤션 참고: `docs/CODE_CONVENTION.md`(레이어별 명명 규칙, 디렉토리 구조, 에러 코드 규칙, Swagger 어노테이션 규칙, 허용 HTTP 상태 코드).

### 응답 & 에러 처리 컨벤션

- 모든 성공 응답은 `ApiResponse.ofSuccess(ResponseCode, data)`를 통해 `ApiResponse<T>`로 감싼다. 실패는 `ApiResponse.ofFail(...)`로 감싼다. 컨트롤러는 성공 시 항상 HTTP `200`을 반환한다 — 허용되는 상태 코드(200/400/401/403/404/405/409)는 `docs/CODE_CONVENTION.md` 5장 참고.
- `ResponseCode`(`global/common`)는 모든 성공/실패 코드를 모아둔 단일 enum으로, 도메인 접두사별(`COMMON-xxx`, `AUTH-xxx` 등)로 그룹화되며 각 항목은 `HttpStatus`, `code`, 한국어 `message`를 가진다.
- 도메인 로직은 `BaseException(ResponseCode)`를 던진다. `GlobalExceptionHandler`(`@RestControllerAdvice`)가 이를 포함해 validation/JSON 파싱/method-not-allowed 같은 프레임워크 예외까지 일관된 `ApiResponse` 형태로 변환한다. 새로운 에러 케이스를 추가할 때는 별도의 예외 타입을 만들기보다 `ResponseCode` 항목을 추가한다.
- Swagger 에러 문서화는 `*Api` 인터페이스 메서드에 선언적으로 붙이는 `@ApiResponseExplanations`/`@ApiErrorResponseExplanation`(`global/annotation/swagger`)을 통해 `ResponseCode` 값을 참조하는 방식으로 이루어진다.

### 인증

`global/security`에서 카카오/구글/네이버 소셜 로그인을 통한 JWT 기반 세션을 처리한다. `JwtAuthenticationFilter`/`JwtExceptionFilter`가 시큐리티 필터 체인(`config/security/SecurityConfig`)에 위치하며, 컨트롤러는 `@AuthenticationPrincipal CustomUserDetails`로 현재 사용자를 받는다. 리프레시 토큰은 별도로 영속화된다(`domains/refreshtoken`). AI 서버에서 오는 서버 간 콜백은 별도의 공유 internal 토큰(`auth.internal-token`)으로 보호한다(`EstimateController#aiCallback` 참고).

### 실시간 채널

- **WebSocket/STOMP**(`global/config/websocket`)는 사용자-이사업체 채팅(`domains/chat`)에 사용된다. 브로커 prefix는 `/sub`, 앱 destination prefix는 `/pub`, 엔드포인트는 `/ws/chat`이며 `StompAuthInterceptor`로 인증한다.
- **SSE**(`global/sse`, `EstimateController#getEstimateSSE`에서 사용)는 비동기로 진행되는 AI 분석 동안 견적서 처리 상태를 클라이언트에 push하는 데 사용된다. 클라이언트는 SSE가 완료를 알리면 전체 견적서를 다시 조회해야 한다.

### 견적서 생명주기 (핵심 플로우)

1. 클라이언트가 이미지를 업로드하면 `infra/s3`가 저장 후 URL을 반환하고, `EstimateUseCase#createAndAnalyze`가 `Estimate`를 생성하면서 비동기 AI 분석을 시작한다(`AsyncConfig`, `infra/ai`).
2. 클라이언트가 기본 정보를 입력하고(`PATCH /estimates/{id}`), 감지된 가구 수량을 조정하고(`PATCH /estimates/{id}/furniture`), 견적 항목 수량을 조정한다(`PATCH /estimates/{id}/items`).
3. AI 서버가 콜백을 호출(`POST /estimates/{id}/callback`)하면서 `AIAnalysisResponse`를 전달하고, `EstimateCreatedEvent`/상태 업데이트가 발행되어 SSE를 통해 클라이언트에 전달된다.
4. 클라이언트는 SSE를 구독한 뒤 상태가 완료되면 최종 견적서를 조회한다(`GET /estimates/{id}`).

## Git & PR 컨벤션

전체 규칙: `docs/GIT_CONVENTION.md`. 이 저장소에서 커밋이나 PR을 요청받았을 때 따라야 할 핵심 사항:

- 브랜치: `main`(실서버, tag 기반 배포) ← `dev`(QA/통합) ← `feature/*` / `hotfix/*`. `main`에 직접 커밋하지 않으며, PR은 항상 `dev`를 대상으로 한다.
- 커밋/PR 제목 형식: `<이슈번호> :emoji: <type>: <subject>` — `GIT_CONVENTION.md` 표에 나열된 type/emoji만 사용한다(`feat`✨, `fix`🐛, `docs`📝, `style`🎨, `refactor`♻️, `test`✅, `chore`🔧, `wip`🚧, `rename`🚚). subject는 명령형, 첫 글자 대문자, 마침표 없이 작성하며, 기존 히스토리상 한국어로 작성하는 경우가 많다.
- PR 본문은 `.github/pull_request_template.md` 템플릿을 따른다.
