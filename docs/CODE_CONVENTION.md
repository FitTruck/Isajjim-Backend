# Code Convention

## 1. 명명 규칙

변수, 메서드, 클래스 등에는 일관된 명명 규칙을 적용해야 한다.

명확하고 의미 있는 이름은 코드를 읽기 쉽게 만들며, 거기에 규칙성이 더해지면 팀 전체의 생산성에 도움을 줄 수 있다.

> 변수, 메서드, 클래스의 명칭은 의미를 알 수 없는 축약어를 사용하지 않고 풀어서 작성한다.

---

### 1-1. Layer 별 메서드

Layer에서 사용되는 메서드 명명 규칙이며 예제는 `User`로 작성합니다.

| Method | Controller / Service | Repository |
| --- | --- | --- |
| Data Read | `getUser` | `findByUser` / `countByUser` / `existsByUser` |
| Data Insert | `createUser` | `insertUser` |
| Data Delete | `removeUser` | `deleteUser` |
| Data Modify | `modifyUser` | `updateUser` |
| Event 발행 | `UserEvent` | - |

**Controller ~ Service Layer**의 메서드 명칭과 **Repository Layer**의 메서드 명칭을 **다르게 작성**하여, 비즈니스 로직이 동작하는 메서드인지, 저장소에 Access 하기 위한 기능을 제공하는 메서드인지 구별할 수 있도록 합니다.

> 기능을 명확히 설명할 수 있다면 get, create, remove를 사용하지 않아도 됩니다.

```java
// UserController
@PostMapping
public ResponseDto<Users> signInUser(
        @RequestBody UserRequest.SignInUserDto request
) {
    userService.signInUser(request);
    ...
}

// UserService
public void signInUser(UserRequest.SignInUserDto request) {
    userWriteRepository.insertUser(request);
    ...
}

// UserWriteRepository
void insertUser(request)
```

---

### 1-2. Test 규칙

#### 1-2-1. @DisplayName

JUnit 5의 `@DisplayName` 어노테이션을 활용합니다.

- 명사 나열보다 **문장형**으로 작성
  - `A이면 B이다.` 또는 `A이면 B가 아니고 C다.`
  - "~테스트" 지양

| Bad | Good |
| --- | --- |
| 음료 1개 추가 테스트 | 음료를 1개 추가할 수 있다. |
| 음료를 추가할 수 있다. | 음료를 추가하면 주문 목록에 담긴다. |

- 테스트 행위에 대한 **결과까지** 기술하기
- **도메인 용어** 사용하기 (메서드 관점 → 도메인 정책 관점)

| Bad | Good |
| --- | --- |
| 특정 시간 이전에 주문을 생성하면 실패한다. | 영업 시작 시간 이전에는 주문을 생성할 수 없다. |

#### 1-2-2. BDD 스타일 테스트

- TDD에서 파생된 개발 방법으로, 시나리오 기반 테스트케이스에 집중합니다.
- 개발자가 아닌 사람이 봐도 이해할 수 있을 수준의 추상화를 권장합니다.
- 테스트 자체가 문서의 역할을 할 수 있도록 작성합니다.

| 구분 | 설명 |
| --- | --- |
| **Given** | 시나리오 진행에 필요한 모든 준비 과정 (객체, 값, 상태 등) |
| **When** | 시나리오 행동 진행 |
| **Then** | 시나리오 진행에 대한 결과 명시 및 검증 |

---

### 1-3. 요청/응답 객체 필드명

#### 1-3-1. 공통

- 필드명은 **DB에서 사용하는 이름**을 기준으로 합니다.
  - `Ticket.status` → `status`, `Member.nickname` → `nickname`
- 동일한 필드를 여러 역할로 사용하는 경우, 필드 앞에 식별자를 붙입니다.
  - `Member.nickname` (사용자/담당자 모두 표시) → `userNickname`, `managerNickname`
- **PK는 반드시 도메인명을 붙입니다.**
  - `Ticket.id` → `ticketId`

```java
class Admin {

    @Column(name = "admin_id")
    private Long id;
}
```

#### 1-3-2. 요청 객체

URL에 대상 도메인이 명시되는 경우, 도메인명을 생략합니다. 단, PK는 예외입니다.

```
POST /api/user/tickets
```
```json
{
    "title": "string",
    "content": "string"
}
```
```
GET /api/user/tickets/{ticketId}
```

#### 1-3-3. 응답 객체

- 특정 엔티티의 필드값을 반환하는 경우, 확장성을 고려해 **PK를 함께 반환**합니다.
- List의 경우 필드명은 `{도메인명}s` 형태로 작성합니다. (자료형 명시 X)
- 중첩된 객체의 속성인 경우 엔티티 이름을 생략합니다. 단, PK는 예외입니다.

```json
{
    "tickets": [
        {
            "ticketId": "string",
            "title": "string"
        }
    ]
}
```

#### 1-3-4. 응답 필드 값

Enum 값은 자료형 그대로 반환합니다. (`Enum.name()` 값)

```java
public record TicketExampleResponse(
    Long ticketId,
    TicketStatus status
) {}
```

#### 1-3-5. 페이지네이션 파라미터

| 파라미터 | 이름 |
| --- | --- |
| 페이지 번호 | `page` |
| 페이지 크기 | `size` |
| 정렬 기준 | `sortType` |

---

## 2. 디렉토리 구조 (도메인형)

```
├─domains
│  └─[도메인명]
│     ├─application
│     │  ├─dto
│     │  │  ├─request
│     │  │  └─response
│     │  ├─mapper
│     │  └─usecase
│     ├─domain
│     │  ├─constant
│     │  └─service
│     ├─exception
│     ├─persistence
│     │  ├─entity
│     │  ├─mapper
│     │  └─repository
│     └─presentation
├─global
│  ├─exception
│  ├─response
│  │  └─code
│  ├─utils
│  └─config
└─infra
```

### domains

도메인별 aggregate. (예: `user`, `ticket`)

| 패키지 | 설명 |
| --- | --- |
| `application/dto` | 계층 간 데이터 전송 객체 (request / response 구분) |
| `application/mapper` | 객체 간 변환 로직 (DTO ↔ Entity) |
| `application/usecase` | 하나의 행위에 대한 비즈니스 로직 통합. service 계층에 의존하여 반환 객체를 변환하는 역할 (다른 usecase 참조 지양) |
| `domain/constants` | 상수 클래스 |
| `domain/service` | repository 계층에 의존하며 다양한 비즈니스 로직 제공 |
| `exception` | 도메인 커스텀 예외 정의 |
| `persistence/entity` | DB 테이블과 1:1 매핑되는 JPA 엔티티 |
| `persistence/repository` | JPA 커스텀 메서드 및 쿼리 작성 |
| `presentation` | REST API Controller. usecase를 호출하여 응답 반환 |

### global

| 패키지 | 설명 |
| --- | --- |
| `exception` | 서비스 내 커스텀 에러 표준화 |
| `response` | 서비스 내 공통 응답 표준화 |
| `config` | Configuration 클래스 정의 |
| `jwt` | JWT 관련 기능 |

### infra

외부 DB, 외부 API 서버 등 외부 계층 관련 기능

### utils

helper, parser 등의 부가 기능

---

## 3. 커스텀 에러 코드

세부 도메인별로 `CustomErrorCode`를 작성하여 사용합니다.

트레일링 콤마(마지막 `,`)를 사용하여 새로운 예외 추가 시 해당 줄만 변경 부분으로 인식되도록 합니다.

```java
@Getter
@AllArgsConstructor
public enum DomainErrorCode implements BaseErrorCode {

    BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다"),
    UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "COMMON_401", "인증이 필요합니다"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

> 에러/성공 코드는 도메인별로 `code` 폴더를 만들어 관리합니다.

| 항목 | 규칙 |
| --- | --- |
| `domain` | 관련 도메인(Entity 단위) 이름 |
| `status` | 동일한 status에 여러 enum 값이 존재할 경우 name에 의미를 명시 |
| `code` | 동일 도메인 내 에러 코드 추가 순서. 1부터 시작 |
| `message` | `'입니다.'` 체로 작성 |

---

## 4. Swagger 예외 명시

컨트롤러에 발생 가능한 예외를 명시하여 Swagger에서 한 번에 확인할 수 있도록 합니다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomErrorCodes {

    CommonErrorCode[] commonErrorCodes() default {};

    DomainErrorCode[] domainErrorCodes() default {};
}
```

```java
@PostMapping("/api/tickets")
@CustomErrorCodes(domainErrorCodes = {DomainErrorCode.DOMAIN_STATUS})
@Operation(summary = "티켓 생성", description = "")
public ApplicationResponse<TicketResponse> createTicket(...) {
    ...
}
```

---

## 5. HTTP Status 코드

아래를 제외한 HttpStatus는 사용하지 않습니다. 성공 시에는 무조건 `200`을 반환합니다.

| Code | 의미 | 예시 |
| --- | --- | --- |
| `200` | `OK` 성공 | - |
| `400` | `BAD_REQUEST` 잘못된 요청이나 문법 | ticketId를 빈 문자열로 요청함 |
| `401` | `UNAUTHORIZED` 인증되지 않은 접근 | JWT 없이 요청함 |
| `403` | `FORBIDDEN` 권한 문제 | 다른 사용자의 티켓 ticketId로 요청함 |
| `404` | `NOT_FOUND` 존재하지 않는 객체 | 해당 ticketId를 가진 티켓이 존재하지 않음 |
| `409` | `CONFLICT` 현재 상태와 충돌 | 중복 닉네임으로 회원 등록 요청 / 현재 티켓 상태에서 불가능한 작업 요청 |
