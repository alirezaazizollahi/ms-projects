# Open Questions

Use this file to track unresolved design and implementation questions.

## Architecture

- Should the repository add a root Maven aggregator `pom.xml` for shared dependency management and one-command builds?
- Should service names, artifact IDs, and database names be standardized?
- Will the system use an API gateway? If yes, which gateway?
- Will services use service discovery, static URLs, or container networking?

## Authentication and Authorization

- Is Keycloak the planned identity provider?
- Should `user-ms` own user profile only while authentication is handled externally?
- Should role data come from the local `users` table, Keycloak claims, or both?
- How should internal service calls be authenticated?

## Order and Product Integration

- Should `order-ms` call `product-ms` when adding to cart to validate product existence, active status, stock, and price?
- Should product price be copied into cart at add time, copied into order at checkout time, or always resolved live from product service?
- Should order total multiply price by quantity when storing `totalAmount`?
- How should stock be reserved or decremented when orders are created?

## Data and Database

- Should each service have a separate PostgreSQL database, separate schema, or separate database server?
- Should database names avoid reserved or confusing names such as `order`?
- Which migration tool should be used: Flyway or Liquibase?
- Should IDs remain numeric, or should public APIs use UUIDs?

## API and Validation

- Should all services add Jakarta Bean Validation annotations to request DTOs?
- Should all services use a shared error response format?
- Should non-numeric IDs return `400 Bad Request` instead of uncaught exceptions?
- Should endpoints return created resources, IDs, or simple status messages consistently?

## Testing

- What level of tests should be required before changing a service: unit, slice, integration, contract?
- Should Testcontainers be used for PostgreSQL integration tests?
- Should API contract tests be added between `order-ms`, `product-ms`, and `user-ms` when inter-service calls are introduced?

