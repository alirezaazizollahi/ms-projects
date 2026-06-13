# Project Understanding

## Repository Shape

The repository root contains three Spring Boot microservices and shared infrastructure files:

```text
ms-projects/
  files/
    docker-compose-postgres-pagadmin.yml
  order-ms/
  product-ms/
  user-ms/
```

Each microservice is currently an independent Maven project. There is no parent aggregator `pom.xml` at the repository root yet.

## Architecture

The current architecture is a simple synchronous ecommerce backend split by business capability:

```text
Client
  |
  |-- user-ms    : user profiles and addresses
  |-- product-ms : product catalog
  |-- order-ms   : cart and order placement
```

There is currently no API gateway, service discovery, message broker, shared authentication layer, or inter-service HTTP client implementation in the code. Some comments indicate planned Keycloak and product-service validation work, but the first version stores local IDs and uses fixed placeholder values in the order service.

## Stack

| Area | Current Choice |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring Web MVC starter |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Build Tool | Maven Wrapper per service |
| Boilerplate | Lombok |
| Mapping | MapStruct configured in user/product services; manually mapped in most service code |
| Tests | Spring Boot generated context tests exist per service |
| Local Infra | Docker Compose for PostgreSQL and pgAdmin |

## Service Responsibilities

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
- `order-ms` does not fetch product price or product availability from `product-ms`.
- `order-ms` does not validate user existence against `user-ms`.
- No authentication or authorization is active.
- Keycloak-related code exists only as comments in `user-ms`.
- No migrations tool is configured; Hibernate `ddl-auto: update` manages schema changes.
- Databases named in service configs differ from the Docker Compose default database.
- No root-level build orchestration exists for all services together.

