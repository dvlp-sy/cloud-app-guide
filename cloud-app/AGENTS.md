# Cloud Application Guide 에이전트 지침

## 프로젝트 개요

- 이 프로젝트는 회원 로그인, 상품·재고 관리, 주문·주문 취소 흐름을 제공하는 단일 Spring Boot 웹 애플리케이션이다.
- 실행 기준은 Java 21, Spring Boot 4.1.1, Gradle이며 기본 패키지는 `com.guide.cloud_app`이다.
- Spring MVC, Spring Data JPA/JDBC, Lombok, Thymeleaf, springdoc OpenAPI를 사용한다. 기본 배포 데이터베이스는 MySQL이며 PostgreSQL 드라이버도 선언되어 있다.
- 브라우저 화면은 Thymeleaf로 렌더링하고, `src/main/resources/static/js`의 바닐라 JavaScript로 동작을 보강한다.

## 작업 전 확인

1. 루트의 `harness/goals.md`, `harness/architecture.md`, `harness/quality-gates.md`를 읽는다.
2. 변경 범위에 해당하는 도메인의 엔티티, 서비스, 리포지터리, DTO, 컨트롤러, 템플릿과 스크립트를 함께 확인한다.
3. 기존 변경 사항을 덮어쓰거나 무관한 파일을 정리하지 않는다.
4. 데이터베이스 비밀번호나 환경 변수를 커밋·로그·문서에 노출하지 않는다.

## 저장소 구조

```text
src/main/java/com/guide/cloud_app/
  common/       공통 응답, JPA 감사, Swagger 설정
  member/       회원 엔티티·리포지터리·서비스·컨트롤러
  item/         상품·재고 엔티티·DTO·리포지터리·서비스·컨트롤러
  order/        주문·주문상품 엔티티·DTO·리포지터리·서비스·컨트롤러
src/main/resources/
  application.yaml        DB_* 환경 변수를 사용하는 배포 설정
  application-local.yaml  로컬 MySQL 설정
  templates/               Thymeleaf 화면과 공통 fragment
  static/                  CSS와 브라우저 JavaScript
src/test/java/             테스트 코드
harness/                   프로젝트 목표·구조·품질·브랜치·워크플로 문서
scripts/                   로컬 및 CI 검증 스크립트
```

## 주요 명령

```bash
./gradlew test
./gradlew bootRun
./gradlew bootRun --args='--spring.profiles.active=local'
./scripts/verify.sh
```

`./scripts/verify.sh`가 하네스의 표준 검증 진입점이다. 기능 변경은 최소한 이 스크립트와 `./gradlew test`를 통과해야 한다.

## 백엔드 규칙

- 도메인 우선 패키지 구조를 유지한다. 업무 규칙은 `*Service`, 저장소 접근은 `*Repository`, HTTP 연결은 컨트롤러, 전송 객체는 해당 도메인의 DTO에 둔다.
- 서비스는 Lombok `@RequiredArgsConstructor`로 생성자 주입하고, 재고·주문 변경의 트랜잭션 경계를 보존한다.
- 생성자는 외부에서 직접 호출하지 않도록 접근 수준을 제한하고, 객체 생성은 정적 팩토리 메서드(`of(...)`, `from(...)`, `create(...)` 등)를 통해 표현한다. 생성 규칙이 복잡하면 팩토리 메서드 안에서 검증한다.
- 엔티티와 DTO의 필드는 특별한 변경 사유가 없으면 `final`로 선언해 불변으로 유지한다. 상태 변경이 필요한 엔티티는 setter 대신 의도를 드러내는 도메인 메서드를 제공한다.
- 엔티티는 protected 기본 생성자, private builder, 정적 `of(...)` 팩토리, 상태 변경 도메인 메서드 패턴을 따른다. 단순 매핑을 위해 setter를 노출하지 않는다.
- 스프링 빈(`@Service`, `@Repository`, `@Component` 등)은 싱글턴으로 공유될 수 있으므로 요청별 데이터·사용자 정보·임시 계산 결과를 필드에 보관하지 않는다. 상태는 메서드 지역 변수, 인자, 반환 객체 또는 영속 엔티티에 둔다.
- `Service`와 `Repository`는 CQRS 원칙에 따라 읽기(Query)와 쓰기(Command)의 책임을 분리한다. 조회 메서드는 상태를 변경하지 않고, 변경 메서드는 명확한 명령 이름과 트랜잭션 경계를 갖도록 한다. 한 메서드에서 조회와 변경을 암묵적으로 섞지 않으며, 규모가 커지면 Query/Command 서비스를 별도 클래스로 분리한다.
- API나 화면에 JPA 엔티티를 직접 반환하지 않고 DTO를 사용한다.
- JSON API는 `/api/**` 아래에 두고 `ResponseEntity<ApiResponse<T>>`를 반환한다. 응답 필드와 순서(`isSuccess`, `code`, `message`, `data`)를 유지한다.
- API를 추가·변경할 때 해당 도메인의 `*ApiControllerDocs`와 성공 응답 enum도 함께 갱신한다.
- 현재 오류 처리는 서비스의 `IllegalArgumentException` 중심이다. 전역 예외 처리나 새로운 오류 응답 규격은 기존 `ApiResponse`와의 호환성을 검토한 뒤 도입한다.
- 회원 세션은 현재 `memberId` 쿠키로 표현하며 주문 API는 `@CookieValue`로 이를 읽는다. 인증 구조를 바꾸는 작업이 아니면 이 계약을 유지한다.

## 보존해야 할 도메인 규칙

- 상품 등록은 `Item`과 일대일 `Inventory`를 함께 생성하며, 수량이 0 이하이면 거부한다.
- 주문은 재고 존재·충분 여부를 확인하고 재고를 차감한 뒤 `Orders`와 `OrderItem`을 하나의 트랜잭션에서 생성한다.
- 주문 취소는 `ORDERED` 상태에서만 가능하며 주문한 수량만큼 재고를 복원한다.
- 주문 요청의 `priceSnapshot`은 `OrderItem`에 저장되는 주문 시점 가격 정보다. 현재 상품 가격과 혼동하지 않는다.
- `JpaConfig`의 JPA auditing과 엔티티의 `@CreatedDate`/`@LastModifiedDate` 동작을 보존한다.

## 프론트엔드 규칙

- 공통 레이아웃과 공통 UI는 `src/main/resources/templates/fragments/layout.html`과 `src/main/resources/static/css/style.css`를 기준으로 한다. 헤더, 푸터, 네비게이션, 컨테이너, 버튼, 패널, 표 스타일을 중복 작성하지 않는다.
- 도메인을 확장할 때는 `templates/<domain>/`에 HTML을, `static/css/<domain>/`에 전용 CSS를 새로 구성한다. 공통 파일에는 실제로 여러 도메인이 공유하는 요소만 추가한다.
- 새 도메인 페이지는 `style.css`와 필요한 도메인 전용 CSS를 함께 참조하고, 공통 `layout.html` fragment를 사용한다.
- 페이지별 JavaScript는 `static/js/<domain>/`에 둔다. `fetch` 결과는 HTTP 상태와 `result.isSuccess`를 모두 확인하고 데이터는 `result.data`에서 읽는다.
- 현재 페이지 경로는 `/`, `/login`, `/items`, `/items/{itemId}`, `/orders`, `/orders/{orderId}`다.

## 설정 및 안전

- `application.yaml`은 `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`를 요구하며 배포 설정의 `ddl-auto`는 `validate`다.
- `application-local.yaml`은 로컬 MySQL과 `ddl-auto: update`를 사용한다. 로컬 자격 증명을 소스 코드에 추가하거나 문서에 복사하지 않는다.
- 일반 개발 작업에서 스키마 설정을 임의로 변경하거나 데이터를 삭제하는 명령을 실행하지 않는다.

## 변경 완료 기준

1. 기능·버그 수정에 맞는 테스트를 추가하거나 갱신한다.
2. `./scripts/verify.sh`를 실행한다.
3. API 계약, 트랜잭션, 재고 상태, 화면 레이아웃이 유지되는지 확인한다.
4. `git diff --check`와 변경 파일 목록을 확인한다.
5. 무관한 포맷 변경, 비밀 정보, 생성물과 빌드 디렉터리가 포함되지 않았는지 검토한다.

## 브랜치 및 릴리스 규칙

- `main`: 검증이 완료된 메인 브랜치이며 배포 기준점이다.
- `develop`: 여러 변경사항을 통합하고 통합 검증을 수행하는 브랜치다.
- `feat/OOO`: 새로운 기능을 개발하는 브랜치다. `OOO`에는 짧고 구체적인 기능명을 사용한다.
- `bugfix/OOO`: 기존 동작의 버그를 수정하는 브랜치다. `OOO`에는 수정 대상을 드러내는 이름을 사용한다.
- 기능·버그 수정 브랜치는 `develop`에서 생성하고, 리뷰와 품질 게이트 통과 후 `develop`에 통합한다. 검증된 변경만 `main`에 반영한다.
- `main` 배포는 Semantic Versioning(`MAJOR.MINOR.PATCH`)에 맞는 `vMAJOR.MINOR.PATCH` 태그를 기준으로 한다. 머지는 자동으로 수행하지 않으며, 리뷰어가 확인한 뒤 명시적으로 진행한다.
- `main`에 반영되면 `release.yaml`이 마지막 릴리스 이후 커밋을 기준으로 버전을 자동 계산하고 태그를 생성한다. `BREAKING CHANGE` 또는 `type!:`은 MAJOR, `feat:`은 MINOR, 그 외 변경은 PATCH를 증가시킨다. 따라서 기능·버그 수정 커밋은 Conventional Commits 형식을 사용한다.
- 브랜치 생성·병합·릴리스 절차의 세부 규칙은 `harness/branching.md`와 `harness/workflows/` 문서를 따른다.

## 코드 스타일

- 기존 Java 명명과 포맷을 따른다(`ItemService`, `ItemRequest`, `ItemSuccessResponse` 등).
- 컨트롤러의 직접 상태 변경보다 도메인 메서드와 서비스 규칙을 우선한다.
- 주석은 문법 설명보다 업무 의도를 설명할 때만 짧게 작성한다.
