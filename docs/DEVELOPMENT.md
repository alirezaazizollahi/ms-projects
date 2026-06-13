# Development Guide

## Prerequisites

- Java 25
- Docker and Docker Compose
- Maven Wrapper from each service directory
- PostgreSQL client access or pgAdmin for creating service databases

## Start Local Infrastructure

From repository root:

```bash
docker compose -f files/docker-compose-postgres-pagadmin.yml up -d
```

Then create the databases expected by services:

- `userdb`
- `product`
- `order`

The current Compose file creates only `mydb` automatically.

pgAdmin is available at:

```text
http://localhost:5050
```

Default pgAdmin credentials:

```text
admin@example.com
admin123
```

## Run Services

Use a separate terminal for each service.

User service:

```bash
cd user-ms
./mvnw spring-boot:run
```

Product service:

```bash
cd product-ms
./mvnw spring-boot:run
```

Order service:

```bash
cd order-ms
./mvnw spring-boot:run
```

## Run Tests

Per service:

```bash
./mvnw test
```

Current tests are generated Spring Boot context tests:

- `user-ms/src/test/java/com/raalapp/ecommerce/UserApplicationTests.java`
- `product-ms/src/test/java/com/raalapp/ecommerce/ProductApplicationTests.java`
- `order-ms/src/test/java/com/raalapp/ecommerce/OrderApplicationTests.java`

## Suggested Development Workflow

1. Make code changes in one service at a time.
2. Run that service's tests.
3. Start dependent infrastructure and manually verify API behavior.
4. Update docs in this folder:
   - API changes: [API.md](API.md)
   - Entity or repository changes: [DATA-MODEL.md](DATA-MODEL.md)
   - Business logic changes: [SERVICES.md](SERVICES.md)
   - Config or dependency changes: [CONFIGURATION.md](CONFIGURATION.md)
   - Design choices: [DECISIONS.md](DECISIONS.md)
   - Change summary: [CHANGES.md](CHANGES.md)

## Recommended Next Engineering Improvements

- Add a root Maven aggregator or build script to run all service tests together.
- Add database initialization for `userdb`, `product`, and `order`.
- Replace hardcoded cart price with product lookup from `product-ms`.
- Validate user IDs through `user-ms` or authentication claims.
- Add request validation with Jakarta Bean Validation.
- Add global exception handling for bad IDs and validation failures.
- Add service integration tests for controller and service behavior.
- Decide whether service names and artifact IDs should be consistent.

