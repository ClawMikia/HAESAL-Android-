# HAESAL (햇살) — 외계 사이버펑크 태양광 배터리 모니터

> **햇살** (한국어) = *햇빛*

생산 수준의 안드로이드 애플리케이션으로, 배터리 충전 상태를 시각화하고, 기기의 **조도 센서**를 활용한 지능형 태양광 충전 시뮬레이션을 제공하며, 배터리 이력을 추적하고 통찰력을 제공합니다. 모든 데이터는 미래적인 외계인 사이버펑크 인터페이스를 통해 제시됩니다.

---

## 플랫폼 요구 사항

| 속성 | 값 |
|-----------------|-------------------------------|
| Android Studio | Narwhal 2025.1.3+ |
| Kotlin | 2.0.21 (K2 모드) |
| Gradle | 8.7 |
| 대상 SDK | 34 |
| 최소 SDK | 24 (안드로이드 7.0) |
| JDK | 17 |
| UI | XML 레이아웃 + ViewBinding |
| 아키텍처 | MVVM + Repository + Room |

---

## 프로젝트 구조

```
com.haesal.batterymonitor
├── data
│   ├── model
│   │   ├── BatteryStatus.kt
│   │   ├── ChargingSource.kt
│   │   └── BatteryHistory.kt
│   ├── database
│   │   ├── BatteryDatabase.kt
│   │   └── BatteryHistoryDao.kt
│   └── repository
│       └── BatteryRepository.kt
├── presentation
│   ├── activity
│   │   ├── MainActivity.kt
│   │   ├── SettingsActivity.kt
│   │   └── HistoryActivity.kt
│   ├── adapter
│   │   └── BatteryHistoryAdapter.kt
│   └── viewmodel
│       └── BatteryViewModel.kt
├── util
│   ├── BatteryReceiver.kt
│   ├── BootReceiver.kt
│   ├── PreferencesManager.kt
│   ├── NotificationHelper.kt
│   └── BatteryInfoFormatter.kt
└── HaesalApplication.kt
```

---

## 핵심 기능

### 지능형 태양광 반응로 (Solar Reactor)
- **실시간 조도 센서 연동**: 기기의 주변 광센서(Ambient Light Sensor)를 통해 실제 빛의 세기(Lux)를 측정합니다.
- **AI 반응로 분석**: 감지된 광자 밀도에 따라 AI가 실시간으로 인사이트를 제공합니다 (예: "OVERLOAD: 직접적인 태양광 충격! 에너지 흡수 최대치").
- **가변 효율 애니메이션**: 빛의 세기에 따라 반응로 코어의 회전 속도와 글로우 효과가 동적으로 변화합니다.
- **시뮬레이션 모드**: 설정에서 활성화 시, 충분한 광량이 확보되면 시스템 충전기 없이도 "태양광 충전" 상태로 진입합니다.

### 실시간 배터리 모니터링
- 배터리 퍼센트 및 충전 상태 시각화
- 충전원 감지 (AC / USB / 무선 / 태양광 반응로)
- 배터리 건강 상태, 기술, 온도, 전압의 정밀 HUD 표시

### 배터리 이력 및 통찰력 (Room DB)
- 모든 배터리 이벤트를 자동으로 기록하고 7일간 보관합니다.
- **Reactor Insights**: 평균 레벨, 최고/최저 온도, 가장 빈번한 충전원 등 데이터 분석 결과를 제공합니다.

### 사이버펑크 UI/UX
- **Reactor Visualization**: 펄스 애니메이션과 스캔 라인 효과가 포함된 미래지향적 코어 UI.
- **HUD Panel**: 외계 기술 인터페이스 스타일의 정보 카드.
- **Dark Theme**: 에너지를 절약하고 몰입감을 높이는 딥 블루/골드 팔레트.

---

## 빌드 및 실행

1. Android Studio Narwhal을 엽니다.
2. File → Open → `Haesal` 폴더를 선택합니다.
3. Gradle 동기화가 완료될 때까지 기다립니다.
4. 실제 기기(조도 센서 테스트 권장) 또는 에뮬레이터에서 실행합니다.

---

## 아키텍처 흐름

```
Activity (UI)
    ↑ (StateFlow 관찰)
ViewModel (비즈니스 로직 & 센서 데이터 처리)
    ↓ (데이터 요청)
Repository (데이터 관리 추상화)
    ↓ (데이터 소스)
Room DB / Hardware Sensors (Light Sensor)
```

---

## 색상 팔레트

| 역할 | 헥스 값 | 설명 |
|------------------|-----------|------------------|
| 프라이머리 에너지 | #FFD54F | 반응로 코어 및 주요 수치 |
| 세컨더리 글로우 | #FF9800 | 강조 및 AI 인사이트 |
| 배경 | #050816 | 딥 스페이스 다크 |
| 표면 | #0B1020 | UI 패널 배경 |
| 성공 | #00E676 | 안정적 상태 및 높은 효율 |
| 경고 | #FF5252 | 낮은 에너지 및 위험 상태 |

---

*HAESAL — 외계 기술이 태양 에너지를 만나는 곳.*
