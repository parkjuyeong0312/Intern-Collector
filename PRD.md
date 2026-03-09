# PRD - Career-Crawler (인턴 공고 AI 요약 알림 서비스)

## 1. 제품의 목적 (Product Vision)

매일 수십 개씩 올라오는 인턴 공고를 일일이 확인하는 번거로움을 해결한다.
AI를 활용해 공고의 핵심(기술 스택, 자격 요건)만 3줄 요약하여 디스코드 채널로 실시간 전달함으로써,
사용자가 자신에게 맞는 공고만 빠르게 선별할 수 있도록 돕는다.

---

## 2. 사용자 시나리오 (User Journey)

1. **수집:** 시스템이 정해진 시간(예: 매시간 0분)마다 채용 사이트를 크롤링한다.
2. **필터링:** 이미 저장된 공고(중복)는 제외하고 신규 공고만 추출한다.
3. **AI 요약:** AI가 신규 공고 본문을 읽고 `기술 스택`, `주요 업무`, `우대 사항`을 뽑아낸다.
4. **알림:** 가공된 예쁜 카드 메시지(Embed)가 사용자의 디스코드 채널에 도착한다.
5. **확인:** 사용자는 요약본을 보고 관심이 생기면 원문 링크를 클릭해 지원한다.

---

## 3. 핵심 기능 (Functional Requirements)

| 우선순위 | 기능명 | 상세 설명 |
|---|---|---|
| **P0 (필수)** | **Multi-Site Crawler** | 사람인, 잡코리아 등 주요 타겟 페이지의 HTML 파싱 (Jsoup 활용). |
| **P0 (필수)** | **Duplicate Check** | DB(MySQL)를 조회하여 중복 공고 발송 방지. |
| **P1 (중요)** | **AI Summarizer** | LLM API(OpenAI/Gemini)를 통해 긴 공고문을 정해진 포맷으로 요약. |
| **P1 (중요)** | **Discord Webhook** | 요약된 내용을 디스코드 Embed 형태로 전송. |
| **P2 (선택)** | **Keyword Filter** | "Java", "Spring" 등 특정 키워드가 포함된 공고만 우선 알림. |

---

## 4. 기술 아키텍처 (Technical Architecture)

- **Backend:** Spring Boot 3.x (Java 17+)
- **Database:** MySQL (공고 ID 및 메타데이터 저장)
- **Scheduling:** Spring Scheduler (`@Scheduled`)
- **Data Source:** Zighang REST API (Jsoup 불필요 — 순수 HTTP 호출)
- **External API:** OpenAI / Gemini (공고 분석용), Discord Webhook (알림용)

## 4-1. Zighang API 명세

| 항목 | 값 |
|---|---|
| Base URL | `https://api.zighang.com/api` |
| 목록 엔드포인트 | `GET /recruitments/v3` |
| 인턴 필터 파라미터 | `?employeeTypes=전환형인턴` |
| 페이지네이션 | `?page=0&size=20` (0-indexed) |
| 인턴 공고 수 | 약 651건 / 131페이지 |
| 공고 원문 URL | `https://zighang.com/recruitment/{id}` |

**응답 구조 (공고 1건):**

```json
{
  "id": "7f18e842-...",         // 중복 체크 키
  "company": { "name": "레브잇" },
  "title": "Problem Solver Internship",
  "employeeTypes": ["전환형인턴"],
  "regions": ["서울"],
  "depthOnes": ["AI_데이터"],
  "depthTwos": ["데이터분석가"],
  "keywords": ["이커머스", "월급"],
  "deadlineType": "채용시마감",
  "endDate": null,
  "createdAt": "2025-12-19T08:42:07"
}
```

---

## 5. 데이터 구조 (Data Schema)

```sql
CREATE TABLE intern_post (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    site_platform VARCHAR(50),       -- 채용 사이트 명 (예: Saramin)
    remote_id   VARCHAR(100) UNIQUE, -- 사이트별 고유 공고 번호 (중복 체크용)
    title       VARCHAR(255),
    company     VARCHAR(100),
    tech_stack  TEXT,                -- AI가 추출한 기술 스택
    summary     TEXT,                -- AI가 만든 3줄 요약
    original_url TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 6. 마일스톤 (Milestone)

| 주차 | 목표 |
|---|---|
| **1주차** | 프로젝트 세팅 및 Jsoup 기반 기본 크롤러 구현 (사이트 1곳 타겟) |
| **2주차** | AI API 연동 테스트 및 DB 저장 로직 (중복 체크) 완성 |
| **3주차** | 디스코드 웹후크 연동 및 메시지 UI (Embed) 최적화 |
| **4주차** | AWS EC2 배포 및 주기적 실행 테스트 |

---

## 7. 핵심 주의사항

- **중복 체크(P0)** 가 최우선. 똑같은 공고가 반복 발송되면 사용자는 알림을 꺼버린다.
- **AI 요약 포맷(P1)** 은 고정된 구조(`기술 스택 / 주요 업무 / 우대 사항`)를 유지해야 가독성이 확보된다.
