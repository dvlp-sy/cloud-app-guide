# Code Review Workflow

1. 변경 목적과 범위를 확인한다.
2. `architecture.md`와 `review-checklist.md`를 기준으로 diff를 읽는다.
3. 기능 오류, 데이터 일관성, 보안·비밀 정보, API 호환성, 프론트 레이아웃 순서로 검토한다.
4. `git diff --check`와 `./scripts/verify.sh` 결과를 확인한다.
5. 발견 사항은 파일·라인·재현 조건·개선 방향이 드러나도록 기록한다.
6. 검증되지 않은 가정과 남은 위험을 리뷰 요약에 명시한다.
