# Cloud Application Guide — Agent Instructions

## Project overview

- This is a single Spring Boot web application for a small shopping flow: member login, item/inventory management, and ordering/cancellation.
- The runtime baseline is Java 21, Spring Boot 4.1.1, and Gradle. The application package is `com.guide.cloud_app`.
- The backend uses Spring MVC, Spring Data JPA/JDBC, Lombok, Thymeleaf, and springdoc OpenAPI. MySQL is the configured runtime database; PostgreSQL is also declared as a runtime driver.
- The browser UI is server-rendered with Thymeleaf and enhanced by small vanilla JavaScript modules under `src/main/resources/static/js`.

## Repository layout

```text
src/main/java/com/guide/cloud_app/
  common/       shared API response types and JPA/Swagger configuration
  member/       member entity, repository, service, page/API controllers
  item/         item and inventory entities, DTOs, repository, service, controllers
  order/        order/order-item entities, DTOs, repository, service, controllers
src/main/resources/
  application.yaml        deployment-style configuration using DB_* environment variables
  application-local.yaml   local MySQL profile configuration
  templates/               Thymeleaf pages and layout fragments
  static/                  CSS and browser JavaScript
src/test/java/             Spring context and future integration/unit tests
```

## Important commands

Use the Gradle wrapper from the repository root:

```bash
./gradlew test
./gradlew bootRun
./gradlew bootRun --args='--spring.profiles.active=local'
```

`./gradlew test` is the minimum verification after backend changes. The current test suite is intentionally small and contains a Spring context smoke test, so feature work should add focused tests rather than relying on context loading alone.

## Backend conventions

- Keep the existing domain-first package layout. Put business rules in `*Service`, persistence access in `*Repository`, HTTP wiring in controllers, and transport models in the domain's DTO package.
- Services are constructor-injected with Lombok `@RequiredArgsConstructor` and are class-level `@Transactional`. Preserve transaction boundaries for operations that change inventory and orders.
- Entities use protected no-argument constructors, private builders, static `of(...)` factories, and domain methods for state changes. Avoid exposing setters just to simplify mapping.
- Use DTOs for API/page data rather than returning JPA entities directly. DTOs currently expose fields through Lombok `@Getter` and factory methods.
- JSON APIs live under `/api/**` and return `ResponseEntity<ApiResponse<T>>`. Keep the response envelope fields and order (`isSuccess`, `code`, `message`, `data`) consistent. Add domain-specific success enum values under the domain's `api` package when adding endpoints.
- OpenAPI annotations live in `*ApiControllerDocs` interfaces. Update the corresponding docs interface when changing an API operation; `SwaggerConfig` groups `/api/**` as `cag-api`.
- Existing error handling is based on `IllegalArgumentException` from services/controllers; there is no visible global exception handler. Do not silently introduce a new error contract—coordinate any centralized error handling with the existing `ApiResponse` shape.
- The member session is currently represented by a `memberId` cookie. Order API endpoints read it with `@CookieValue`; preserve that behavior unless authentication is deliberately redesigned.

## Domain rules to preserve

- Item registration creates both an `Item` and its one-to-one `Inventory`; registration rejects non-positive quantity.
- Ordering validates inventory, decrements stock, creates `Orders` and `OrderItem` rows, and calculates the order total inside one transaction.
- Cancellation only succeeds for an `ORDERED` order and restores each ordered quantity to inventory.
- `priceSnapshot` is carried by order requests and persisted on `OrderItem`; do not confuse it with the current item price when changing order pricing behavior.
- JPA auditing is enabled by `JpaConfig`; preserve `@CreatedDate`/`@LastModifiedDate` behavior on audited entities.

## Frontend conventions

- Thymeleaf page routes are `/`, `/login`, `/items`, `/items/{itemId}`, `/orders`, and `/orders/{orderId}`. Templates belong under matching folders in `templates/`.
- Treat `src/main/resources/static/css/style.css` and `src/main/resources/templates/fragments/layout.html` as the source of truth for shared layout, typography, navigation, buttons, containers, panels, tables, and footer structure. Reuse their classes and fragments instead of duplicating common markup or styles.
- When extending a domain, create domain-specific HTML templates under `templates/<domain>/` and domain-specific stylesheets under `static/css/<domain>/`. Keep domain markup and styling out of the shared layout files unless the change is genuinely cross-domain.
- Each new domain page should include the shared `layout.html` fragments and link its own domain stylesheet in addition to `style.css` when domain-specific styling is needed.
- Browser code uses `fetch`, checks both HTTP success and `result.isSuccess`, and reads payloads from `result.data`. Use DOM APIs and text assignment patterns that preserve the existing safe rendering style.
- Keep page-specific scripts in the matching `static/js/<domain>/` folder and page-specific CSS in `static/css/<domain>/`.

## Configuration and safety

- `application.yaml` expects `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`; deployment uses `ddl-auto: validate`.
- `application-local.yaml` is for a local MySQL instance and uses `ddl-auto: update`. Treat database credentials as sensitive; do not copy them into commits, logs, tests, or documentation. Prefer environment variables or a local untracked override when changing local setup.
- Do not change schema-affecting settings, delete data, or run destructive database commands as part of ordinary development.

## Change workflow

1. Inspect the relevant domain's entity, service, repository, DTO, controller, and page/script before editing.
2. Make the smallest change that preserves the existing API envelope and transaction behavior.
3. Add or update tests for business rules, especially stock decrement/restoration, invalid quantities, member validation, and order status transitions.
4. Run `./gradlew test`; if database-backed tests are added, document the required profile/database setup.
5. Review the diff for accidental credential exposure and unrelated formatting changes.

## Style

- Follow the existing Java formatting and naming (`ItemService`, `ItemRequest`, `ItemSuccessResponse`, etc.).
- Prefer clear domain methods over inline mutation from controllers.
- Keep comments short and explain business intent, not obvious syntax. Korean user-facing messages should remain consistent with the surrounding code.
