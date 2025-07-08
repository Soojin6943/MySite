# MySite

개인 연습용으로 만든 웹 프로젝트입니다.  
여러가지 해보고 싶은 기술 및 기능을 구현해보며 학습한 내용을 정리하고, 실제 동작하는 형태로 만들어보는 것을 목표로 했습니다.
<br>


## 🛠️ 기술 스택

- Java 17  
- Spring Boot  
- Spring MVC  
- Spring Security  
- JPA (Hibernate)  
- MySQL  
- JWT  
- Swagger  
- OAuth2 (Kakao)
<br>

## 🚧 구현 순서 및 설명

아래 순서대로 개발하며 리팩토링과 보안 강화를 진행했습니다.

---

### 1. 🍪 쿠키 로그인 (Cookie-Based Login)

- 사용자 정보를 쿠키에 직접 저장
- 간단한 로그인 기능 구현
- 보안이 취약한 방식임을 직접 체험

---

### 2. 🧾 세션 로그인 (Session-Based Login)

- 서버 측 세션을 이용한 로그인 처리
- 세션 유지 및 만료 처리 구현
- 로그인 후 인증 상태 유지

---

### 3. 🔐 Spring Security 로그인

- Spring Security를 도입하여 인증/인가 처리
- 로그인/로그아웃 기능을 필터 기반으로 구성
- CSRF 방어, 비밀번호 암호화 등 보안 강화

---

### 4. 🔑 JWT 로그인

- 토큰 기반 인증 방식 적용
- JWT 토큰 생성, 검증, 갱신 로직 구현
- 서버 무상태(stateless) 구조 학습

---

### 5. 🟡 카카오 소셜 로그인

- Kakao Developers API 연동
- OAuth2 기반 소셜 로그인 구현
- JWT 토큰 발급과 통합

---

### 6. 🛡️ JWT + Spring Security 통합 로그인

- Spring Security와 JWT를 통합하여 보안 강화
- 커스텀 필터 적용
- 인증/인가를 보다 체계적으로 관리

---

### 7. 📘 Swagger 문서화

- Swagger를 통해 API 문서 자동화
- Swagger UI를 활용한 API 테스트 지원
- 엔드포인트별 요청/응답 명세 작성

---

## 📌 앞으로 계획 중인 기능

- 리프레시 토큰을 이용한 재발급 시스템
- 관리자 기능 추가 (권한별 페이지 접근)
- 게시판 및 방명록
- 쇼핑몰
- 클론 코딩
- 알림 기능
- 부하테스트 및 모니처링
- Redis 세션 관리
- CI/CD 적용

---

