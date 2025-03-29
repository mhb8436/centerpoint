# Centerpoint Server

Centerpoint의 서버 백엔드 애플리케이션입니다. Android와 iOS 클라이언트에 필요한 API를 제공하며, 지하철 노선 정보를 관리하고 최적의 중간 지점을 계산하는 서비스를 제공합니다.

## 프로젝트 구조

```
server/
├── centerpoint/      # 메인 서버 애플리케이션
│   ├── src/         # 소스 코드
│   ├── pom.xml      # Maven 설정
│   └── README.md    # 서버 상세 문서
└── crawler/         # 지하철 노선 정보 크롤러
    ├── src/         # 크롤러 소스 코드
    └── pom.xml      # 크롤러 Maven 설정
```

## 기술 스택

- **언어**: Java 11
- **프레임워크**: 
  - Spring Boot 2.7.x
  - Spring Security
  - Spring Data JPA
- **데이터베이스**:
  - PostgreSQL 13
  - Redis (캐싱)
- **기타 도구**:
  - Maven
  - JUnit 5
  - Mockito
  - Swagger/OpenAPI

## 주요 기능

1. **인증 및 권한 관리**
   - OAuth2 기반 인증
   - JWT 토큰 관리
   - 사용자 권한 관리

2. **위치 기반 서비스**
   - 위도/경도 좌표 변환
   - 주소 검색 API
   - 지하철역 정보 관리

3. **중간 지점 계산**
   - 가중치 기반 중간 지점 계산
   - 3차원 구면 좌표계 변환
   - 다중 참여자 위치 처리

4. **대중교통 경로**
   - 지하철 노선 정보 관리
   - 최단 경로 탐색
   - 환승 정보 계산

## 개발 환경 설정

1. **필수 도구**
   - JDK 11
   - Maven 3.8.x
   - PostgreSQL 13
   - Redis 6.x

2. **환경 변수 설정**
   ```bash
   export JAVA_HOME=/path/to/jdk11
   export MAVEN_HOME=/path/to/maven
   export PATH=$PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin
   ```

## 빌드 및 실행

1. **데이터베이스 설정**
   ```sql
   CREATE DATABASE centerpoint;
   CREATE USER centerpoint WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE centerpoint TO centerpoint;
   ```

2. **애플리케이션 빌드**
   ```bash
   cd server/centerpoint
   ./mvnw clean install
   ```

3. **실행**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **크롤러 실행**
   ```bash
   cd server/crawler
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## API 문서

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 테스트

```bash
# 단위 테스트 실행
./mvnw test

# 통합 테스트 실행
./mvnw verify
```

## 배포

1. **JAR 파일 생성**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **실행**
   ```bash
   java -jar target/centerpoint-0.0.1-SNAPSHOT.jar
   ```

## 모니터링

- Actuator 엔드포인트: `http://localhost:8080/actuator`
- Prometheus 메트릭: `http://localhost:8080/actuator/prometheus`
- Health 체크: `http://localhost:8080/actuator/health`

## 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 프로젝트 루트의 [LICENSE](../LICENSE) 파일을 참조하세요. 