# SmartOrder — B2B Procurement Platform

A production-grade backend built to demonstrate Java Tech Lead capabilities:
hexagonal architecture, microservices, cloud-native deployment on Azure, and event-driven design.

**Stack:** Java 21 · Spring Boot 3.3 · PostgreSQL · Docker · GitHub Actions · Azure (Month 2+)

---

## Architecture

SmartOrder follows hexagonal architecture (ports and adapters):

```
domain/          ← pure Java, zero framework dependencies
  model/         ← aggregates, value objects, domain exceptions
  port/          ← inbound ports (use cases) + outbound ports (repositories)

application/     ← use case implementations, orchestration
  usecase/

infrastructure/  ← adapters: JPA, REST controllers, messaging (Month 2+)
  persistence/
  web/
```

The domain layer has **no Spring, no JPA, no framework** — only Java.
This is intentional. It means every domain test runs in milliseconds with no application context.

---

## Running locally

**Prerequisites:** Java 21, Maven, Docker

```bash
# Start Postgres
docker compose up postgres -d

# Run the app (dev profile)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run all tests (Testcontainers handles its own Postgres)
./mvnw verify
```

---

## Architecture decisions

All significant decisions are documented in [`docs/adr/`](docs/adr/).

| ADR | Decision |
|-----|----------|
| [ADR-001](docs/adr/ADR-001-hexagonal-architecture.md) | Hexagonal architecture as base style |

---

## Learning journal

Session notes and weekly retrospectives: [`docs/journal.md`](docs/journal.md)
