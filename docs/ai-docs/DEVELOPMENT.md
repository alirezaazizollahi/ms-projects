# Development Guide

## Local startup order

The services are independent Maven projects. For an integrated local run:

1. Start PostgreSQL, MongoDB if needed by user service, and RabbitMQ.
2. Start `eureka`; confirm `http://localhost:8761` is reachable.
3. Start `configserver`; check
   `http://localhost:8888/order-service/default` returns configuration.
4. Start `product-ms`, `user-ms`, then `order-ms`.

Config imports are optional, but services can still fail later if a required
database or broker is unavailable. `order-ms` has a Eureka client wiring issue
documented in [STARTUP-TROUBLESHOOTING.md](STARTUP-TROUBLESHOOTING.md).

Each project can be launched from its directory with `./mvnw spring-boot:run`.
Use `./mvnw test` there to run that project's tests. This guide does not claim
those commands were run during repository exploration.

## Prerequisites

- Java 25
- Docker (if using local containerized infrastructure)
- Maven Wrapper from each service directory
- PostgreSQL client access or pgAdmin for creating service databases
- RabbitMQ on `localhost:5672` for Spring Cloud Bus refresh flows

## Start Local Infrastructure

This repository currently contains no Docker Compose file. Configure PostgreSQL
databases `products` and `orders` plus the MongoDB database used by user service
according to the active config files. Start RabbitMQ at `localhost:5672` for
Spring Cloud Bus refresh flows.

## Run Services

Use a separate terminal for each service.

Config server:

```bash
cd configserver
./mvnw spring-boot:run
```

The config server currently runs with the `native` profile and loads configuration from `configserver/src/main/resources/config`. `APP_CONFIGURATION_TOKEN` is only needed if the commented Git backend is re-enabled.

`config-demo` is an optional config client demonstration, separate from the
ecommerce service startup flow.

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

- `configserver/src/test/java/com/raalapp/ConfigserverApplicationTests.java`
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
- Externalize config server keystore password and alias before production use.
- Add request validation with Jakarta Bean Validation.
- Add global exception handling for bad IDs and validation failures.
- Add service integration tests for controller and service behavior.
- Decide whether service names and artifact IDs should be consistent.
