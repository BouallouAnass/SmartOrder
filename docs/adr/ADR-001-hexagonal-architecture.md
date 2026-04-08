# ADR-001: Hexagonal Architecture as the structural pattern

## Status
Accepted

## Context
SmartOrder is a B2B procurement platform expected to grow into microservices.
The initial monolith must be structured to allow that transition without
rewriting business logic. The team has historically built layered monoliths
which couple domain logic to infrastructure concerns.

## Decision
Adopt Hexagonal Architecture (Ports and Adapters). The domain model and use
cases live in the core with zero dependencies on frameworks or infrastructure.
Inbound ports define what the application can do. Outbound ports define what
the application needs. Adapters implement those ports.

## Consequences
- Domain logic is fully testable without Spring context or database
- Adding a new delivery mechanism (REST, CLI, messaging) requires only a new adapter
- More initial structure overhead compared to a simple layered approach
- Developers unfamiliar with the pattern need onboarding