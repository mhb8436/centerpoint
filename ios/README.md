# Centerpoint iOS

Centerpoint의 iOS 클라이언트 애플리케이션입니다. 친구들과의 약속 장소를 정할 때 최적의 중간 지점을 찾아주는 서비스를 제공합니다.

## 프로젝트 구조

```
ios/
├── centerpoint/      # 메인 애플리케이션 소스 코드
│   ├── AppDelegate.swift
│   ├── SceneDelegate.swift
│   ├── Views/        # UI 컴포넌트
│   ├── Models/       # 데이터 모델
│   ├── ViewModels/   # 뷰 모델
│   ├── Services/     # 비즈니스 로직
│   └── Utils/        # 유틸리티 함수
└── icons/           # 앱 아이콘 및 이미지 에셋
```

## 기술 스택

- **언어**: Swift 5.0
- **최소 iOS 버전**: 13.0
- **주요 프레임워크**:
  - UIKit
  - MapKit
  - CoreLocation
  - Combine
  - SwiftUI (일부 컴포넌트)
  - Alamofire
  - Kingfisher
  - SnapKit

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
   - Xcode 15.0 이상
   - CocoaPods
   - iOS 13.0 이상 시뮬레이터

2. **CocoaPods 설치**
   ```bash
   sudo gem install cocoapods
   ```

## 빌드 및 실행

1. **프로젝트 클론**
   ```bash
   git clone [repository-url]
   cd ios
   ```

2. **의존성 설치**
   ```bash
   pod install
   ```

3. **실행**
   - `centerpoint.xcworkspace` 파일을 Xcode로 열기
   - 시뮬레이터 선택 후 Run 버튼 클릭 (⌘R)

## 테스트

```bash
# 단위 테스트 실행
xcodebuild test -workspace centerpoint.xcworkspace -scheme centerpoint -destination 'platform=iOS Simulator,name=iPhone 14'

# UI 테스트 실행
xcodebuild test -workspace centerpoint.xcworkspace -scheme centerpointUITests -destination 'platform=iOS Simulator,name=iPhone 14'
```

## 릴리즈 빌드

1. **Archive 생성**
   - Xcode에서 Product > Archive 선택
   - Archive 완료 후 Distribute App 선택

2. **App Store Connect 업로드**
   - Xcode Organizer에서 Archive 선택
   - Distribute App > App Store Connect 선택
   - 인증서 및 프로비저닝 프로파일 확인

## 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 프로젝트 루트의 [LICENSE](../LICENSE) 파일을 참조하세요. 