# 아키텍처 및 변경 규칙

## 계층

`Controller → Service → Repository → Entity` 흐름을 기본으로 한다. DTO는 Controller와 Service 사이의 입력·출력 계약을 담당하며, Entity를 웹 응답으로 직접 노출하지 않는다.

## 객체 생성과 불변성

- 생성자는 외부에 직접 노출하지 않는다. 객체 생성 의도와 검증 규칙을 표현할 수 있도록 `of(...)`, `from(...)`, `create(...)` 같은 정적 팩토리 메서드를 사용한다.
- 필드는 특별한 변경 사유가 없는 한 불변으로 유지한다. 생성 이후 변경이 필요한 엔티티도 setter 대신 `cancel()`, `addQuantity()`처럼 업무 의도를 표현하는 메서드로 제한적으로 변경한다.

## 스프링 빈 상태

스프링 빈은 기본적으로 싱글턴으로 공유되므로 공유 상태를 보유하지 않는다. 요청·사용자·계산 중간값을 서비스나 리포지터리의 인스턴스 필드에 저장하지 않고 메서드 인자와 지역 변수로 전달한다. 캐시나 누적 통계처럼 공유 상태가 꼭 필요하면 명시적인 동시성·수명·무효화 설계를 별도로 문서화한다.

## CQRS 원칙

Service와 Repository는 명령(Command)과 조회(Query)의 책임을 분리한다.

- Query는 데이터를 읽기만 하며 엔티티·재고·주문 상태를 변경하지 않는다.
- Command는 생성·수정·삭제·상태 전이를 수행하며 변경 의도가 메서드 이름에 드러나야 한다.
- 한 메서드에서 조회 결과를 이용한 암묵적 변경을 수행하지 않는다. 변경이 필요하면 명시적인 Command 흐름으로 분리한다.
- 작은 규모에서는 같은 클래스 안에서 `get...`/`find...`와 `create...`/`update...`/`cancel...` 메서드를 분리할 수 있다. 책임이 커지면 Query Service와 Command Service, 또는 읽기 Repository와 쓰기 Repository를 별도 타입으로 분리한다.
- CQRS 적용은 트랜잭션 경계와 도메인 불변식을 약화시키지 않아야 한다. 주문·재고처럼 함께 변경되어야 하는 작업은 하나의 Command 트랜잭션으로 처리한다.

## 도메인

- `member`: 회원 생성·로그인·회원 검증, `memberId` 쿠키 처리
- `item`: 상품 정보와 일대일 재고 관리
- `order`: 주문·주문상품 생성, 재고 차감·복원, 주문 상태 관리
- `common`: `ApiResponse`, 성공·실패 응답 계약, JPA auditing, Swagger 설정

## API 계약

API 응답은 `ApiResponse<T>`를 사용한다. 성공·실패 여부, 코드, 메시지, 선택적 데이터를 유지한다. API 경로를 바꾸면 `*ApiControllerDocs`, 성공 응답 enum, 관련 JavaScript 호출부와 화면을 함께 검토한다.

## 화면 구조

공통 fragment는 `templates/fragments/layout.html`, 공통 CSS는 `static/css/style.css`다. 도메인별 템플릿과 CSS는 각각 `templates/<domain>/`, `static/css/<domain>/` 아래에 둔다. 페이지별 JS는 `static/js/<domain>/` 아래에 둔다.

## 데이터와 트랜잭션

상품 등록, 주문, 주문 취소는 관련 엔티티와 재고가 일관된 상태로 저장되도록 트랜잭션 안에서 처리한다. 주문 금액은 주문상품 금액의 합으로 계산하며, 취소 시 주문상품 수량만큼 재고를 복원한다.

## 설정

배포 설정은 환경 변수 기반 `application.yaml`, 로컬 개발은 `application-local.yaml`을 사용한다. 자격 증명은 커밋하지 않는다.
