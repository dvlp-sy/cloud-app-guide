# 브랜치 및 릴리스 전략

## 브랜치 종류

| 브랜치 | 목적 | 기준·병합 방향 |
| --- | --- | --- |
| `main` | 검증된 메인·배포 기준점 | 리뷰 확인 후 `develop`에서 수동 병합 |
| `develop` | 변경사항 통합 및 통합 검증 | `feat/*`, `bugfix/*`를 리뷰 후 수동 병합 |
| `feat/OOO` | 신규 기능 개발 | `develop`에서 생성, 작업 완료 후 `develop`으로 병합 |
| `bugfix/OOO` | 버그 수정 | `develop`에서 생성, 작업 완료 후 `develop`으로 병합 |

`OOO`는 소문자 kebab-case를 권장한다. 예: `feat/item-pagination`, `bugfix/order-stock-restore`.

## 병합 원칙

- CI는 테스트와 검증만 수행한다.
- 자동 승인과 자동 병합을 사용하지 않는다.
- 작성자가 품질 게이트 결과와 리뷰 체크리스트를 확인한 뒤 리뷰어가 변경 내용을 검토한다.
- `develop`에서 통합 검증을 마친 뒤에만 `main` 반영을 검토한다.
- 충돌 해결 후에는 해결된 파일과 영향받은 도메인을 다시 검토하고 검증을 재실행한다.

## Semantic Versioning 자동 릴리스

배포 버전은 [Semantic Versioning 2.0.0](https://semver.org/) 규칙을 따른다.

```text
vMAJOR.MINOR.PATCH
```

- `MAJOR`: 호환되지 않는 API·계약 변경
- `MINOR`: 하위 호환되는 기능 추가
- `PATCH`: 하위 호환되는 버그 수정

`main`에 변경이 반영되면 `release.yaml`이 마지막 버전 태그 이후의 커밋 메시지를 분석해 다음 태그를 자동 생성한다.

- `BREAKING CHANGE`, `BREAKING-CHANGE`, `feat!:` 등 호환성을 깨는 변경: MAJOR 증가
- `feat:`: MINOR 증가
- `fix:`, `docs:`, `refactor:` 등 그 외 변경: PATCH 증가

예를 들어 현재 버전이 `v1.2.3`이고 이후 커밋에 `feat: 상품 검색 추가`가 있으면 `v1.3.0` 태그가 생성된다. `v1.2.3` 태그가 생성되면 `cd.yaml`이 테스트와 패키징을 수행하고 JAR 산출물을 보관한다. 실제 클라우드 배포 단계는 배포 대상과 자격 증명 정책을 확정한 뒤 CD 워크플로에 추가한다.

자동화는 `main` 반영 후 태그 생성까지만 수행하며, 브랜치 병합·승인·리뷰는 자동화하지 않는다. 커밋 메시지는 다음 형식을 권장한다.

```text
feat(item): 상품 검색 추가
fix(order): 취소 시 재고 복원 오류 수정
refactor: 응답 매핑 구조 정리
feat!: 주문 API 응답 계약 변경
```
