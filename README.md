<img width="1279" height="589" alt="서비스화면" src="https://github.com/user-attachments/assets/1010c960-982c-4baf-8101-66f643569daa" />

## 📑 목차
1. [💡 이삿찜 소개](#-이삿찜-소개)
2. [✨ 주요 기능](#-주요-기능)
3. [🏗️ 아키텍처](#-아키텍처)
4. [🛠️ 기술 스택](#-기술-스택)
5. [💻 개발 환경 구축](#-개발-환경-구축)
6. [🚀 CI/CD](#-cicd)
7. [👥 이삿찜을 만든 사람들](#-이삿찜을-만든-사람들)

<br/>

## 💡 이삿찜 소개
**이삿찜**은 사진 한 장으로 이사를 혁신하는 **AI 이사 견적 서비스**입니다.

## ✨ 주요 기능
### 1. AI 기반 이사 견적 산출
사용자가 집 사진을 업로드 하기만 하면 AI가 가구를 분석하여 이사에 필요한 트럭 개수를 산출하고, 3D 시뮬레이션을 통해 가구가 트럭에 어떻게 배치되는지 확인할 수 있습니다.

### 2. 최저가 업체 매칭 (구현 예정)
견적 생성을 완료하면 이사 업체별로 예상 가격을 사용자에게 보내고, 사용자는 그 중 최저가 업체 또는 마음에 드는 업체와 채팅을 통해 최종 견적을 확정합니다.

## 🏗️ 아키텍처
 <img width="1316" height="854" alt="image" src="https://github.com/user-attachments/assets/be435782-e6cc-4418-b921-083e75981131" />

## 🛠️ 기술 스택
BE : Spring Boot, Spring JPA, Google Cloud Platform(Cloud Run, Cloud Storage, Gemini API)
<br/>
FE : React Native
<br/>
AI : FastAPI, Google Cloud Platform(Compute Engine), Yoloe, SAM3D

## 💻 개발 환경 구축
- 본 프로젝트는 보안을 위해 민감한 설정값을 .env 파일로 관리합니다. 로컬 개발 환경을 구축하기 위해 아래 과정을 따라주세요.
    - 환경 변수 파일 생성
        - 루트 디렉토리의 .env.example 파일을 복사하여 .env 파일을 생성합니다.
        - 각 변수에 로컬 환경에 맞는 값을 입력합니다.
    - IDE 설정 (IntelliJ IDEA 기준)
        - EnvFile 플러그인을 설치합니다.
        - Run/Debug Configuration -> Edit Configuration -> Enable EnvFile, + 버튼 클릭하여 .env 파일을 추가합니다.
- 관련 파일 : .env.example, application.yml

## 🚀 CI/CD
- Github Push -> Cloud Build 트리거(Dockerfile 멀티 스테이지 빌드) -> Artifact Registry에 이미지 저장 -> Cloud Run에서 가져와 서버 실행

## 👥 이삿찜을 만든 사람들
| BE 이효재 | FE 최유준 | AI 김이든 | AI 한윤택 |
|:-:|:-:|:-:|:-:|
| <img src="https://avatars.githubusercontent.com/u/66837740?v=4" width="80"/> | <img src="https://avatars.githubusercontent.com/u/21029157?v=4" width="80"/> | <img src="https://avatars.githubusercontent.com/u/94510368?v=4" width="80"/> | <img src="https://avatars.githubusercontent.com/u/195436700?v=4" width="80"/> |
| [@hyotatoFrappuccino](https://github.com/hyotatoFrappuccino) | [@db-66](https://github.com/db-66) | [@YIDEUNKIM](https://github.com/YIDEUNKIM) | [@HanGloss](https://github.com/HanGloss) |
