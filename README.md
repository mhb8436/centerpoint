# Centerpoint

Centerpoint는 크로스 플랫폼 모바일 애플리케이션 프로젝트입니다. Android, iOS, 그리고 서버 백엔드로 구성되어 있습니다. 이 앱은 친구들과의 약속 장소를 정할 때, 각자의 위치를 고려하여 최적의 중간 지점을 찾아주는 서비스를 제공합니다.

## 프로젝트 구조

```
centerpoint/
├── android/          # Android 애플리케이션
├── ios/             # iOS 애플리케이션
└── server/          # 서버 백엔드
    ├── centerpoint/ # 메인 서버 애플리케이션
    └── crawler/     # 크롤러 서비스
```

## 핵심 기능

### 중간 지점 계산 알고리즘
Centerpoint는 다음과 같은 방식으로 최적의 중간 지점을 계산합니다:

1. **가중치 기반 중간 지점 계산**
   - 각 참여자의 위치를 위도/경도 좌표로 변환
   - 각 참여자별 가중치를 적용 (기본값: 1.0)
   - 3차원 구면 좌표계를 사용하여 정확한 지구상의 위치 계산
   - 가중 평균을 통해 최종 중간 지점 도출

2. **대중교통 최적 경로 탐색**
   - 지하철 노선 정보를 활용한 최단 경로 탐색
   - 환승 횟수를 최소화하는 경로 탐색
   - 도시별 지하철 노선 정보 관리
   - BFS(너비 우선 탐색) 알고리즘을 활용한 경로 탐색

3. **비용 최적화**
   - 환승 시 추가 비용 고려 (기본 환승 가중치: 8)
   - 경로별 환승 가중치 적용 (기본값: 2)
   - 도시 간 이동 시 최적 경로 탐색

## 기술 스택

### Android
- Gradle 4.0.1
- Android Support Library 23.2.0
- JUnit 4.12 (테스팅)
- Android Test Support Library 0.5

### iOS
- Swift 기반 iOS 애플리케이션
- iOS 네이티브 UI 컴포넌트

### Server
- Java 기반 백엔드 서버
- Spring Boot 프레임워크
- OAuth2 인증 서버
- RESTful API 서버
- 크롤러 서비스
- 지하철 노선 데이터 관리 시스템

## 시작하기

### Android
```bash
cd android
./gradlew build
```

### iOS
```bash
cd ios
pod install
open centerpoint.xcworkspace
```

### Server
```bash
cd server/centerpoint
./mvnw spring-boot:run
```

## 개발 환경 설정

### Android
- Android Studio
- JDK 8 이상
- Android SDK

### iOS
- Xcode
- CocoaPods
- iOS 13.0 이상

### Server
- JDK 11 이상
- Maven
- Spring Boot 2.x

## 라이선스
이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

```
MIT License

Copyright (c) 2024 Centerpoint

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.