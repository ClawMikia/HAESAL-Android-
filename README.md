# HAESAL (햇살) — 외계 사이버펑크 태양광 배터리 모니터

> **햇살** (한국어) = *햇빛*

생산 수준의 안드로이드 애플리케이션으로, 배터리 충전 상태를 시각화하고, 외부 태양광 충전(시뮬레이션)을 감지하며, 배터리 이력을 추적하고, 통찰력을 제공하며, 모든 데이터를 미래적인 외계인 사이버펑크 인터페이스를 통해 제시합니다.

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

## 기능

### 실시간 배터리 모니터링
- 배터리 퍼센트 (실시간)
- 충전 상태 (충전 중 / 방전 중)
- 충전원 (AC / USB / 무선 / 태양광)
- 배터리 건강 상태
- 배터리 기술
- 온도 (°C)
- 전압 (V)

### 태양광 모드
- 설정에서 전환 가능
- 활성화 시 AC/USB/무선 대신 "태양광 충전" 표시
- 상태에 반응하는 애니메이션된 태양광 반응로 시각화
- 배터리 100% 충전 시 특별한 태양광 충전 완료 알림 표시

### 배터리 이력 (Room 데이터베이스)
- 배터리 상태 변경 자동 기록
- 7일이상된 기록 자동 삭제
- HistoryActivity에서 전체 이력 목록

### 배터리 통찰력
- 총 기록 수
- 평균 배터리 레벨
- 충전 세션 횟수
- 최고/최저 배터리 레벨
- 평균 온도 및 전압
- 가장 일반적인 충전원

### 알림
- 배터리 충전 완료 (100%)
- 태양광 충전 완료 (태양광 모드에서)
- 배터리 저충전 (< 20%)
- 충전 시작
- 자동 알림: 사용자 권한 없이 시스템 알림으로 표시됨

### 애니메이션
- 반응로 핵심 펄스
- 태양광 회전선
- 에너지 스캔 라인
- 반응로 바깥 고리 펄스

---

## 빌드 및 실행

1. Android Studio Narwhal 엽니다
2. File → Open → `Haesal` 폴더 선택
3. Gradle 동기화 완료까지 대기
4. 에뮬레이터 또는 기기 (API 24+)에서 실행

---

## 아키텍처

```
Activity / Fragment
    ↓  observe StateFlow
ViewModel
    ↓  calls
Repository
    ↓  queries
Room Database  ←→  BroadcastReceiver
```

모든 비즈니스 로직은 ViewModel 및 Repository 레이어에 있습니다.  
Activity에는 UI 연결 및 탐색만 포함됩니다.

---

## 색상 팔레트

| 역할 | 헥스 값 |
|------------------|-----------|
| 프라이머리 에너지 | #FFD54F |
| 세컨더리 글로우 | #FF9800 |
| 배경 | #050816 |
| 표면 | #0B1020 |
| 카드 | #111827 |
| 성공 | #00E676 |
| 경고 | #FF5252 |
| 텍스트 프라이머리 | #FFFFFF |
| 텍스트 세컨더리 | #B0BEC5 |

---

*HAESAL — 외계 기술이 태양 에너지를 만나는 곳.*
