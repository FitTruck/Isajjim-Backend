# Onboarding

이삿찜 백엔드에 처음 합류한 팀원을 위한 시작 가이드입니다. 코드/컨벤션 자체는 아래 문서들이 이미 정리되어 있으니 여기서는 "어디서 시작해서 어디를 봐야 하는지"만 안내합니다.

- 프로젝트 개요, 아키텍처, 도메인 구조: [`CLAUDE.md`](../CLAUDE.md)
- 코드 컨벤션(레이어별 명명 규칙, 디렉토리 구조, 에러 코드, Swagger 규칙): [`docs/CODE_CONVENTION.md`](./CODE_CONVENTION.md)
- Git/PR/커밋 컨벤션: [`docs/GIT_CONVENTION.md`](./GIT_CONVENTION.md)

---

## 1. 로컬 환경 세팅

1. 리포지토리 클론 후 `.env.example`을 복사해 `.env` 생성
   ```bash
   cp .env.example .env
   ```
2. Notion-BE DOCS-secret 문서에서 환경 변수를 복사하여 채웁니다.
3. IntelliJ에서 **EnvFile 플러그인**을 설치하고 Run/Debug Configuration에 `.env`를 연결합니다.
   > Gradle/Spring Boot가 `.env`를 자동으로 읽지 않기 때문에 플러그인 없이 `./gradlew bootRun`을 돌리면 환경변수가 비어서 기동에 실패합니다.
4. Firebase 키 파일을 `configs/firebaseKey.json` 경로에 둡니다 (Notion-BE DOCS-secret 문서 참조)
5. 기본 활성 프로파일은 `local`(`application.yml` → `application-local.yml`)로 설정해주세요. 로컬 런타임 DB는 H2를 쓰지만, `.env.example`에는 MySQL 접속 정보(`LOCAL_DB_*`)도 있으니 로컬에 MySQL/Redis를 직접 띄워서 쓰는 경우 해당 값도 채워야 합니다.
6. 실행/테스트:
   ```bash
   ./gradlew build
   ./gradlew bootRun
   ./gradlew test
   ```
7. API 문서 : http://localhost:8080/swagger-ui/index.html#/

---


## 2. 브랜치 전략 & GitHub 사용법

전체 규칙은 [`docs/GIT_CONVENTION.md`](./GIT_CONVENTION.md) 참고. 핵심만 요약하면:

- 브랜치 구조: `main`(실서버, tag 기반 배포) ← `dev`(QA/통합) ← `feature/*` / `hotfix/*`
- `main`, `dev`에는 직접 커밋하지 않습니다. 모든 작업은 `feature/*` 또는 `hotfix/*` 브랜치에서 시작합니다.

**작업 흐름 예시:**
```bash
# 1. 이슈 확인 후 dev에서 브랜치 생성
git checkout dev
git pull origin dev
git checkout -b "feat/mail-function"

# 2. 작업 & 커밋 (커밋 메세지 컨벤션은 GIT_CONVENTION.md 참고)
git add ...
git commit -m "#12 :sparkles: feat: 메일 발송 기능 추가"

# 3. 푸시 후 dev로 PR 생성 (.github/pull_request_template.md 양식 사용)
git push origin feat/mail-function
```
- PR 제목 형식: `<이슈번호> :emoji: <type>: <subject>` — 허용된 type/emoji 목록은 GIT_CONVENTION.md 표 참고
- 팀원 절반의 승인 후 병합, 병합 후 작업 브랜치는 삭제
- `main`으로의 반영은 `dev → main` PR + 버전 태그(`v{Major}.{Minor}.{Patch}`)로 이루어집니다

---

## 3. 배포 전략

CI/CD는 GitHub Actions로 이루어지며 워크플로우 파일은 `.github/workflows/`에 있습니다.

- **`ci.yml`**: 재사용 워크플로우. JDK 17 세팅 → `./gradlew build` → Docker 이미지 빌드(멀티플랫폼) → `ghcr.io`에 push. `cd.yml`에서 호출됩니다.
- **`cd.yml`**: `dev` 브랜치 push 또는 `v*.*.*` 태그 push 시 트리거.
  - `ci.yml`을 호출해 이미지를 빌드/푸시
  - `dev` push → dev 환경에 배포, `v*` 태그 push → (태그가 `main`에 존재하는지 확인 후) prod 환경에 배포
  - 배포 시 `.env`/`firebaseKey.json`을 GitHub Secrets(`ENV`, `FIREBASE_KEY`, base64 인코딩)에서 복원해 서버로 SCP 전송 후 `scripts/deploy.sh` 실행 (docker-compose 기반)

**릴리즈 절차 (main 배포):**
```bash
git checkout main
git pull origin main
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
# cd.yml이 태그를 감지해 prod 배포

# 이후 dev에 main 변경사항 sync
git checkout dev
git merge main
git push origin dev
```
버전 규칙: `Major`(하위 호환 깨지는 변경), `Minor`(하위 호환되는 기능 추가), `Patch`(버그 픽스).


---
