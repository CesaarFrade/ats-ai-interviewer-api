# AGENTS.md

ATS & AI Interviewer API — Spring Boot backend for an applicant tracking system.

## README is stale — trust the code/config

The README describes an aspirational design (Java 21, Spring AI/LangChain4j, hexagonal architecture). None of that exists yet. The actual project is a plain layered Spring MVC + JPA REST API:

- **Java 17** toolchain, **Spring Boot 4.1.0**, single-module **Gradle 9.5.1** (wrapper)
- Layout: `controller` → `service` (`I*Service` interface + `*Service` impl) → `repository` (Spring Data JPA) → `model` (JPA entities)
- No AI, no hexagonal ports/adapters, no Swagger, no CI, no lint/format config

## Build & test

```sh
docker compose up -d            # start Postgres (ats-postgres on :5432)
.\gradlew.bat build             # Windows; use ./gradlew on Unix
.\gradlew.bat bootRun           # starts app on :8080
.\gradlew.bat test
```

- Only test is `AtsApiApplicationTests` (`@SpringBootTest` contextLoads). It **requires Postgres to be running** — the datasource is loaded, so `gradlew test` fails without the DB up.
- No `gradlew` on PATH; always use the wrapper.

## Database

- PostgreSQL 15 via `docker-compose.yml`. Compose reads `DB_USER`/`DB_PASSWORD`/`DB_NAME` from `.env` (gitignored; copy `.env.example`).
- **Gotcha:** `src/main/resources/application.yml` hardcodes the same credentials and is *not* wired to env vars. If you change `.env`, you must update `application.yml` too. Current creds: `ats_user` / `ats_super_secreto_123` / `ats_database`.
- `spring.jpa.hibernate.ddl-auto: update` — schema is auto-generated from entities at startup. There is no Flyway/Liquibase, so never write migration files; model changes apply directly on restart.

## Conventions

- Code, comments, and API response strings are in **Spanish** (e.g. controllers return `"¡Oferta guardada con éxito..."`). Match this when adding endpoints/messages.
- Services are injected into controllers via Lombok `@RequiredArgsConstructor` + `final` fields.
- Entities use Lombok `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`; fields are camelCase.
- Service interfaces are prefixed `I` (e.g. `IOfertaService`).
- REST base path is `/api/<recurso>` (`/api/ofertas`). Only `OfertaController` has real endpoints; `CandidatoController`, `CVController`, and `PostulacionController` are empty stubs — check before assuming an endpoint exists.
- `com.cesarfrade.ats.exception.NotFoundException` is thrown by services but there is **no `@ControllerAdvice`**, so it currently surfaces as a generic error. Add a handler if you add endpoint behaviors depending on it.
