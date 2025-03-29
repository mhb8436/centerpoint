# Centerpoint Android

Centerpoint의 Android 클라이언트 애플리케이션입니다. 친구들과의 약속 장소를 정할 때 최적의 중간 지점을 찾아주는 서비스를 제공합니다.

## 프로젝트 구조

```
android/
├── app/              # 메인 애플리케이션 모듈
├── swipe-selector/   # 커스텀 스와이프 선택기 컴포넌트
├── sweetsheet/       # 커스텀 바텀시트 컴포넌트
└── centerpoint.jks   # 서명 키스토어
```

## 기술 스택

- **언어**: Kotlin
- **최소 SDK**: API 24 (Android 7.0)
- **타겟 SDK**: API 34 (Android 14)
- **주요 라이브러리**:
  - AndroidX Core KTX
  - AndroidX AppCompat
  - Material Design Components
  - Google Maps SDK for Android
  - Retrofit2
  - OkHttp3
  - Coroutines
  - ViewModel & LiveData
  - Room Database

## 주요 기능

1. **위치 기반 서비스**
   - 현재 위치 확인
   - 지도 상 위치 선택
   - 주소 검색 및 자동완성

2. **중간 지점 계산**
   - 다중 참여자 위치 입력
   - 가중치 기반 중간 지점 계산
   - 지도 상 시각화

3. **대중교통 경로**
   - 지하철 노선 정보 표시
   - 최적 경로 탐색
   - 환승 정보 제공

## 개발 환경 설정

1. **필수 도구**
   - Android Studio Hedgehog | 2023.1.1
   - JDK 17
   - Android SDK

2. **환경 변수 설정**
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/tools
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```

## 빌드 및 실행

1. **프로젝트 클론**
   ```bash
   git clone [repository-url]
   cd android
   ```

2. **의존성 설치**
   ```bash
   ./gradlew build
   ```

3. **실행**
   - Android Studio에서 프로젝트를 열고 Run 버튼 클릭
   - 또는 터미널에서:
     ```bash
     ./gradlew installDebug
     ```

## 테스트

```bash
# 단위 테스트 실행
./gradlew test

# UI 테스트 실행
./gradlew connectedAndroidTest
```

## 릴리즈 빌드

```bash
# 릴리즈 APK 생성
./gradlew assembleRelease

# 릴리즈 번들 생성
./gradlew bundleRelease
```

## 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 프로젝트 루트의 [LICENSE](../LICENSE) 파일을 참조하세요. 