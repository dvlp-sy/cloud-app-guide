# Feature Workflow

1. `develop`에서 `feat/OOO` 브랜치를 생성한다.
2. `goals.md`, `architecture.md`, `quality-gates.md`와 관련 도메인 코드를 읽는다.
3. API, 화면, 데이터 변경 범위를 먼저 정리한다.
4. 도메인 규칙을 Service와 Entity 메서드에 반영하고 Controller는 얇게 유지한다.
5. 프론트엔드는 공통 layout·CSS를 재사용하고 도메인 전용 파일을 별도로 만든다.
6. 회귀 테스트와 새 동작 테스트를 추가한다.
7. `./scripts/verify.sh`를 실행한다.
8. `review-checklist.md`로 자체 리뷰하고 변경 요약과 검증 결과를 남긴다.
9. 확인된 변경만 리뷰를 거쳐 `develop`에 수동으로 병합한다.
