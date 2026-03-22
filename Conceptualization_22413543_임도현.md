> # 1. Conceptualization

## YU Queue System


| Category | Information |
| :--- | :--- |
| **Student No** | `22413543` |
| **Name** | `임도현` |
| **E-mail** | `gusehdla@naver.com` |


> # 1. Business purpose

매년 수강신청 기간마다 대학가는 보이지 않는 전쟁터가 됩니다. 평상시보다 약 100배 이상의 트래픽이 단 1초 사이에 동시에 집중되면서 기존의 관계형 데이터베이스 기반 시스템은 한계에 직면합니다. 실제로 초당 10,000건 이상의 동시 접속 요청이 발생할 경우, 서버의 응답 시간은 평소보다 20배 이상 늘어나며 이 과정에서 서버 다운이나 접속 지연과 같은 치명적인 장애가 반복되고 있습니다. 이러한 불확실성은 사용자로 하여금 무분별한 새로고침을 시도하게 만들며, 이는 다시 서버 부하를 가중시키는 악순환으로 이어집니다. 본 프로젝트는 이러한 불안정한 환경을 기술적 수치로 제어하고 극복하기 위해 시작되었습니다.

현재 시스템의 가장 큰 문제점은 정보의 불투명성에서 오는 리소스 낭비입니다. 학생들은 단순히 로딩 화면을 바라보며 기다릴 뿐, 자신의 대기 순번이나 남은 좌석 정보를 실시간으로 확인하기 어렵습니다. 대기자가 정원보다 2배 이상 많은 상황에서도 사용자는 이를 인지하지 못한 채 평균 3분 이상의 무의미한 대기 시간을 소비하게 됩니다. 저희는 개발자 지망생으로서 이러한 정체 구간을 데이터로 시각화하고, 시스템이 감당할 수 있는 초당 처리량을 사전에 정의하여 전체 가용성을 확보할 수 있는 기술적 해결책을 제시하고자 합니다.

본 프로젝트의 최우선 목표는 고가용성 대기열 시스템의 구축을 통한 서비스 안정화입니다. 레디스 기반의 대기열 설계를 통해 초당 50,000건 이상의 요청을 유실 없이 수용하고, 메인 데이터베이스에는 초당 500건 수준의 안정적인 트래픽만 전달하도록 유량 제어를 실시합니다. 또한 사용자에게 1초 단위로 업데이트되는 실시간 대기 순번을 제공하여 본인의 신청 가능성을 즉각 판단할 수 있게 돕고, 가망 없는 대기를 스스로 철회하게 함으로써 유효 트래픽 처리 효율을 30% 이상 향상시키는 것을 목표로 합니다.

이 시스템의 일차적인 대상은 매 학기 수강신청 지연과 장애로 인해 불편을 겪는 전국 대학교의 학생들입니다. 또한 기존 시스템의 노후화로 인해 동시 접속자 처리에 한계를 느끼는 교육 기관 및 대학 행정 부처가 주요 타겟이 될 것입니다.

> # 2. System context diagram

<img width="574" height="300" alt="Image" src="https://github.com/user-attachments/assets/c0910eeb-08ea-4b89-80f5-1e3d992b93b2" />

- **Login** : 로그인
- **Membership** : 학번 기반 회원가입
- **My Page** : 내 수강신청 내역 조회

- **Search Course** : 전체 강의 목록 및 실시간 잔여석 조회
- **Enter Queue** : 신청 버튼 클릭 시 대기열 진입
- **Inform Rank** : 내 앞의 대기 인원 조회 및 실시간 대기 순번 조회
- **Strategic Cancel** : 대기 순번 확인 후 가망 없을 시 즉시 대기 취소

- **Batch Registration** : 대기열 순번에 따른 실제 DB 승인 처리 
- **Check Duplicate** : 동일 과목 및 시간대 중복 신청 방지 로직
- **Result Notification** : 신청 성공/실패 알림

- **Course Registration** : 강의 등록
- **Course Termination** : 폐강 처리
- **Capacity Adjustment** : 정원 수정


> # 3. Use case list

### 1. Membership (회원가입)
- **Actor**: Student
- **Description**: 시스템을 이용하기 위해 신규 회원은 자신의 정보를 등록한다. 학번, 성명, 이메일 등 본인 인증 및 수강 관리를 위한 필수 정보를 입력하여야 한다.

### 2. Login (로그인)
- **Actor**: Student, Administrator
- **Description**: 등록된 학번과 비밀번호를 통해 시스템에 접속한다. 인증 후 세션 또는 토큰을 발급받아 수강신청 및 관리 권한을 획득한다.

### 3. My Page (내 정보 조회)
- **Actor**: Student
- **Description**: 본인이 성공적으로 신청한 강의 목록을 확인하고, 현재 대기 중인 과목의 실시간 상태를 종합적으로 조회한다.

### 4. Search Course (강의 조회)
- **Actor**: Student
- **Description**: 현재 개설된 전체 강의 목록을 확인한다. 대기열에 저장된 실시간 데이터를 바탕으로 각 과목의 잔여 좌석 수를 즉각적으로 파악한다.

### 5. Enter Queue (대기열 진입)
- **Actor**: Student
- **Description**: 수강신청 버튼을 클릭하여 대기열에 진입한다. DB에 직접 부하를 주지 않고 Redis Sorted Set에 접속 기록을 남겨 자신의 대기 순번을 부여받는다.

### 6. Inform Rank (실시간 순번 조회)
- **Actor**: Student
- **Description**: 대기열 진입 후 내 앞에 남은 대기 인원을 실시간으로 확인한다. 시스템의 처리 속도에 따라 줄어드는 순번을 모니터링한다.

### 7. Strategic Cancel (전략적 취소)
- **Actor**: Student
- **Description**: 자신의 순번과 잔여석을 비교하여 가망이 없다고 판단될 경우 대기를 취소한다. 취소 즉시 대기열에서 제거되어 시스템 자원 낭비를 방지한다.

### 8. Batch Registration (배치 승인 처리)
- **Actor**: System (Scheduler)
- **Description**: 대기열 상위 사용자들을 일정 주기마다 추출하여 실제 DB에 수강 내역을 저장한다. 이를 통해 급격한 트래픽으로부터 메인 DB를 보호한다.

### 9. Check Duplicate (중복 검증)
- **Actor**: System
- **Description**: 실제 수강 처리 단계에서 동일 과목 중복 신청 여부나 시간표 중복 여부를 검증하여 데이터 정합성을 유지한다.

### 10. Result Notification (결과 알림)
- **Actor**: System
- **Description**: 배치가 완료되면 학생에게 최종 성공 여부 또는 실패 사유를 실시간으로 피드백한다.

### 11. Course & Capacity Management (강의 및 정원 관리)
- **Actor**: Administrator
- **Description**: 관리자는 신규 강의를 등록(`Registration`)하거나 폐강(`Termination`) 처리하며, 필요에 따라 실시간으로 수강 정원을 수정(`Adjustment`)한다.


> # 4. Concept of operation

