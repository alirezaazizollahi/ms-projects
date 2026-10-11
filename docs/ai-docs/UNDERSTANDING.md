# Project Understanding

## Repository Shape

The repository root contains Spring Boot microservices, a Spring Cloud Config Server, and shared infrastructure files:

```text
ms-projects/
  configserver/
  eureka/
  config-demo/
  order-ms/
  product-ms/
  user-ms/
```

Each microservice is currently an independent Maven project. There is no parent aggregator `pom.xml` at the repository root yet.

## Architecture

The current architecture is a simple ecommerce backend split by business capability, with a config server added for centralized external configuration:

```text
                    +--> user-service (8082)
Client --> services +--> product-service (8083) <-- order-service (8084)
                    +--> order-service (8084)

All service configs --> configserver (8888)
All service registration/discovery --> eureka (8761)
```

Eureka discovery and RabbitMQ-backed Spring Cloud Bus dependencies are present. The order service has a load-balanced HTTP client for product lookups. There is no API gateway or active shared authentication layer. The config server is configured with the `native` profile and serves files from `configserver/src/main/resources/config`.

## Stack

| Area | Current Choice |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring Web MVC starter |
| Persistence | Spring Data JPA / Hibernate |
| Persistence | PostgreSQL configs for product/order; MongoDB URI configured for user |
| Build Tool | Maven Wrapper per service |
| Boilerplate | Lombok |
| Mapping | MapStruct configured in user/product services; manually mapped in most service code |
| External Config | Spring Cloud Config Server in `configserver` |
| Tests | Spring Boot generated context tests exist per service |
| Discovery | Eureka Server on port `8761` |
| Messaging/config refresh | RabbitMQ-backed Spring Cloud Bus (localhost defaults) |

## Service Responsibilities

### Config Server

Owns externalized configuration delivery:

- Runs on port `8888`.
- Uses Spring Cloud Config Server with `@EnableConfigServer`.
- Reads configuration from native classpath files under `configserver/src/main/resources/config`.
- Keeps the previous Git backend settings commented in `application.yaml` for future remote repository use.
- Enables RSA-backed encryption using `config-server.jks`.

### User Service

Owns user profile data:

- User identity fields stored locally.
- Address stored through a one-to-one relationship.
- Role enum with `CUSTOMER` and `ADMIN`.
- CRUD-like operations for list, read, create, and update.

### Product Service

Owns product catalog data:

- Product name, description, price, stock quantity, category, image URL.
- `active` flag for soft delete.
- Search by active product name with stock greater than zero.

### Order Service

Owns cart and order data:

- Cart items are stored locally by `userId` and `productId`.
- Orders are created from current cart items.
- Order creation clears the user's cart.
- Product price is currently hardcoded to `1000.00` when adding to cart.

## Important Current Limitations

- No service-to-service validation is implemented yet.
- `order-ms` has a product-service client, but cart price is still hardcoded; verify live request behavior before relying on the client for business rules.
- `order-ms` does not validate user existence against `user-ms`.
- No authentication or authorization is active.
- The order service currently exposes Eureka client warnings because its global load-balanced `RestClient.Builder` is also selected by Eureka's client; see [STARTUP-TROUBLESHOOTING.md](STARTUP-TROUBLESHOOTING.md).
- Keycloak-related code exists only as comments in `user-ms`.
- No migrations tool is configured; Hibernate `ddl-auto: update` manages schema changes.
- No Docker Compose file is currently present in this repository.
- No root-level build orchestration exists for all services together.
- Config server keystore password and alias are currently stored in application YAML and should be externalized before production use.
