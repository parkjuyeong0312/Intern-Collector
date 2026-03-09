# Intern Collector

> 인턴 공고를 자동 수집하고 AI로 요약해서 디스코드로 알려주는 Spring Boot 서비스

## 개요

매시간 Zighang API에서 인턴 공고를 수집하고, AI(GPT)로 핵심 내용을 요약하여 Discord 채널에 전송합니다.
이미 알림을 보낸 공고는 DB에서 중복 체크하여 재발송하지 않습니다.

## 흐름

```
[Zighang API] → [중복 체크 (MySQL)] → [AI 요약 (GPT)] → [Discord Webhook]
     ↑
  매시 정각 스케줄러 실행
```

## 기술 스택

| 분류 | 기술 |
|---|---|
| Backend | Spring Boot 3.4, Java 17 |
| DB | MySQL |
| Scheduling | Spring Scheduler |
| HTTP Client | RestTemplate |
| AI | OpenAI GPT-4o-mini |
| 알림 | Discord Webhook |

## 프로젝트 구조

```
src/main/java/com/intern/collector/
├── api/                    # Zighang API 클라이언트 & DTO
│   ├── ZighangApiClient.java
│   └── ZighangResponse.java
├── domain/                 # JPA Entity & Repository
│   ├── InternPost.java
│   └── InternPostRepository.java
├── scheduler/              # 수집 스케줄러
│   └── InternCollectScheduler.java
├── discord/                # Discord Webhook 전송
├── config/                 # Bean 설정
└── InternCollectorApplication.java
```

## 환경 변수

`application.yml`에서 아래 환경변수를 참조합니다.

| 변수명 | 설명 |
|---|---|
| `DB_USERNAME` | MySQL 사용자명 |
| `DB_PASSWORD` | MySQL 비밀번호 |
| `DISCORD_WEBHOOK_URL` | Discord Webhook URL |
| `OPENAI_API_KEY` | OpenAI API 키 |

## 실행 방법

```bash
# 1. MySQL 데이터베이스 생성
mysql -u root -p -e "CREATE DATABASE intern_collector;"

# 2. 환경변수 설정 후 실행
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
export DISCORD_WEBHOOK_URL=https://discord.com/api/webhooks/...
export OPENAI_API_KEY=sk-...

# 3. 빌드 & 실행
./gradlew bootRun
```

## API 명세 (Zighang)

- **Base URL:** `https://api.zighang.com/api`
- **목록:** `GET /recruitments/v3?employeeTypes=전환형인턴&page=0&size=20`
- **공고 URL:** `https://zighang.com/recruitment/{id}`

## 마일스톤

- [x] 프로젝트 세팅 & API 명세 파악
- [ ] Zighang API 수집 & DB 저장
- [ ] AI 요약 연동 (GPT-4o-mini)
- [ ] Discord Webhook 알림
- [ ] AWS EC2 배포
