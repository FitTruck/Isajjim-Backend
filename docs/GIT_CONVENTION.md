# Git Convention

## Git Flow Strategy

### 브랜치 구조

| 브랜치 | 역할 |
| --- | --- |
| `main` | 실서버 배포 브랜치 |
| `dev` | 배포 및 QA 브랜치 |
| `feature/*` | 기능 개발 브랜치 |
| `hotfix/*` | 긴급 버그 수정 브랜치 |

---

### main

실서버 배포가 이루어지는 브랜치입니다. 언제나 배포 가능한 상태로 유지되어야 하며, `main` 소스가 변경될 때 `tag`를 생성합니다.

> `main` 브랜치에서는 실제 소스 수정 및 개발을 진행하지 않습니다.

```bash
# dev -> main으로 PR 생성 -> 병합
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
# -> cd.yml이 tag 감지 -> prod 배포

# dev에 main sync (merge commit 반영)
git checkout dev
git merge main
git push origin dev
```

**버전 전략** : `v{Major}.{Minor}.{Patch}`

| 구분 | 설명 |
| --- | --- |
| `Major` | 하위 호환 안 되는 큰 변경 (API 스펙 변경, DB 구조 대규모 변경, 앱 강제 업데이트 필요) |
| `Minor` | 하위 호환 되는 기능 추가 (스프린트 단위 기능 배포) |
| `Patch` | 버그 픽스, 소소한 수정 |

---

### dev

배포 및 QA가 이루어지는 브랜치입니다. `feature` 브랜치에서 개발이 완료된 기능이 올라오는 브랜치입니다.

해당 브랜치로의 직접 커밋은 금지합니다.

---

### hotfix

개발이 완료된 프로젝트의 긴급한 수정사항이나 버그 수정 시 사용하는 브랜치입니다.

---

### feature

기능을 개발하는 브랜치입니다.

- 브랜치 이름은 `<타입>/<작업 내용 요약>` 으로 네이밍합니다.


```bash
git checkout -b "feat/mail-function"
```

---

## Pull Request Convention

### PR 전략

`feature` 또는 `hotfix` 브랜치에서 개발이 완료된 후 dev로 병합하는 PR을 요청합니다. (main으로 직접 접근 불가)

팀원 1/2의 승인이 이루어진 경우 병합 후 작업 브랜치를 삭제합니다.

### PR 제목

PR 제목은 커밋 메세지의 `subject` 형식을 동일하게 따릅니다.

```
:Emoji: <type>: <subject>
ex) :sparkles: feat: 로그인 기능 구현
```

### PR 본문

PR 본문은 `.github/pull_request_template.md` 템플릿을 따릅니다.

---

## Commit Message Convention

---

### 커밋 메세지 포맷

```
:Emoji: <type>: <subject>
ex) :sparkles: feat: 로그인 기능 구현

#<body>
- <Scope>
    - description

#<footer>
```

---

### Type & Emoji

> IntelliJ에서 Gitmoji 플러그인을 사용하려면:
> **Settings → Plugins → Marketplace → Gitmoji 검색 → Install → Restart IDE**

> **⚠️ 아래 표에 나열된 Type과 Emoji 외에는 사용하지 않습니다.**

| Type | Emoji | Description |
| --- | --- | --- |
| `feat` | ✨ sparkles | 새로운 기능 추가 |
| `fix` | 🐛 bug | 기존 기능 및 버그 수정 |
| `docs` | 📝 memo | 문서 수정 |
| `style` | 🎨 art | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 |
| `refactor` | ♻️ recycle | 코드 리팩토링 (주석 제거도 포함) |
| `test` | ✅ white_check_mark | 테스트 코드, 리팩토링 테스트 코드 추가 |
| `chore` | 🔧 wrench | 빌드 업무 수정, 패키지 매니저 수정 |
| `wip` | 🚧 construction | 완료되지 않은 작업 임시 커밋 (가능하다면 지양) |
| `rename` | 🚚 truck | 파일 또는 폴더명 수정하거나 옮기는 경우 |

---

### Subject

해당 커밋에 대한 간단한 한 줄 요약 내용을 작성합니다.

- 50자를 넘기지 않고, 마침표 및 특수기호를 사용하지 않습니다.
- 영문으로 시작하는 경우 동사(원형)를 가장 앞에 두고 첫 글자는 대문자로 작성합니다. (과거시제 사용 금지)
- 이슈 번호를 제목 앞에 붙입니다.

```bash
# Bad
:bug: fix: 버그 수정

# Good
#1 :bug: fix: 버그 수정
```

---

### Body (선택사항)

해당 커밋에 대한 상세 내용을 작성합니다.

- 모든 커밋에 작성할 필요는 없습니다.
- 한 줄에 72자를 넘기지 않습니다.
- **어떻게(how)** 보다 **무엇(what)** 과 **왜(why)** 를 설명합니다.

```
:sparkles: feat: 게시글 작성 API 추가

<body>
- PostController.java
    - 게시글 생성 api 작성
- PostService.java
    - 게시글 생성 로직 작성
- UserRepository.java
    - 작성자 검색용 method 작성
```

---

### Footer (선택사항)

커밋 메세지의 맺음말입니다.

- 모든 커밋에 작성할 필요는 없습니다.
- 이슈를 추적하기 위한 ID를 추가할 때 사용합니다.

| 키워드 | 설명 |
| --- | --- |
| 해결 | 해결한 이슈 ID |
| 관련 | 해당 커밋에 관련된 이슈 ID |
| 참고 | 참고할만한 이슈 ID |

```
<footer>
- 해결: #123
- 관련: #321
- 참고: #222
```

---

### 전체 커밋 메세지 예시

```
WRKR-1 :sparkles: feat: 게시글 작성 API 추가

<body>
- PostController.java
    - 게시글 생성 api 작성
- PostService.java
    - 게시글 생성 로직 작성
- UserRepository.java
    - 작성자 검색용 method 작성

<footer>
- 해결: #123
- 관련: #321
- 참고: #321
```
