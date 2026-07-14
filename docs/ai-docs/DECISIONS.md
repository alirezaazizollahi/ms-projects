# Decisions

Record important architecture and implementation decisions here. Each entry should explain the context, decision, and consequence.

## 2026-05-20: Create Initial Project Documentation Set

Context:

- The project is in its first version.
- Developers and AI assistants need a fast way to understand services, dependencies, APIs, entities, and current gaps.

Decision:

- Add a root `docs/` folder with focused markdown files for overview, service internals, API, data model, configuration, development workflow, decisions, change log, and open questions.

Consequence:

- Future changes should update documentation alongside code.
- `CHANGES.md` becomes the lightweight running history.
- `DECISIONS.md` becomes the place for architectural reasoning instead of burying decisions in code comments.

## 2026-07-14: Move AI Documentation Under `docs/ai-docs`

Context:

- The repository documentation is intended to be maintained as AI-readable project context.
- Keeping that context in a dedicated subdirectory makes its purpose clearer.

Decision:

- Move the existing markdown documentation set from `docs/` to `docs/ai-docs/`.

Consequence:

- Future AI documentation updates should be made under `docs/ai-docs/`.
- Non-markdown operational helper files can remain directly under `docs/` when appropriate.

## 2026-07-14: Add Git-Backed Spring Cloud Config Server

Context:

- Services need a central place to retrieve externalized configuration.
- The configuration source is a Git repository.
- Sensitive properties need encryption support.

Decision:

- Add `configserver` as an independent Spring Cloud Config Server running on port `8888`.
- Configure the server to read from `https://github.com/alirezaazizollahi/app-configuration.git` on label `master`.
- Read the Git access token from `APP_CONFIGURATION_TOKEN`.
- Configure RSA-backed property encryption with `config-server.jks`.

Consequence:

- Runtime startup now requires `APP_CONFIGURATION_TOKEN` when the config server needs authenticated Git access.
- The keystore enables encrypted property workflows through Spring Cloud Config.
- Keystore secrets are still present in YAML and should be externalized before production use.

## Current Architecture: Independent Spring Boot Maven Services

Context:

- `user-ms`, `product-ms`, and `order-ms` each have their own Maven wrapper, `pom.xml`, application class, and configuration.

Decision:

- Treat each directory as an independently runnable microservice.

Consequence:

- Services can evolve separately.
- There is currently no root command to build or test all services.
- Dependency and plugin versions must be kept aligned manually until an aggregator or shared parent is introduced.

## Current Persistence Choice: PostgreSQL with Hibernate `ddl-auto: update`

Context:

- All services use Spring Data JPA and PostgreSQL.
- No migration tool is configured yet.

Decision:

- Use Hibernate schema update for the first version.

Consequence:

- Early development is faster.
- Schema history is not explicit.
- Production use should introduce a migration tool such as Flyway or Liquibase.

## Current Cross-Service References: Store IDs Only

Context:

- `order-ms` needs to associate carts and orders with users and products.
- There is no active service-to-service communication yet.

Decision:

- Store `userId` and `productId` as primitive IDs in `order-ms`.

Consequence:

- Order service remains decoupled at the database level.
- It cannot currently guarantee that referenced users/products exist.
- Product price and availability are not enforced by order service.

## Current Product Deletion: Soft Delete

Context:

- Product data may be referenced by carts or historical orders.

Decision:

- `DELETE /api/products/{id}` sets `active = false` instead of removing the row.

Consequence:

- Product list and lookup endpoints only return active products.
- Historical references can remain meaningful.
