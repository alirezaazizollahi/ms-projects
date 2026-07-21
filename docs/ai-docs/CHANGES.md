# Changes

Use this file as the human-readable change log for the project. Add entries for code, configuration, database, API, dependency, and documentation changes.

## 2026-07-18

### Added

- Added native `configdemo` configuration files under `configserver/src/main/resources/config`.

### Changed

- Switched the config server to the `native` profile with `classpath:/config` as the search location.
- Commented the previous Git-backed config server backend for future remote repository use.
- Updated `configdemo` defaults to use native build metadata instead of environment placeholder values.

## 2026-07-21

### Added

- Added Spring Cloud Bus AMQP support to `configserver` and `config-demo`.
- Added RabbitMQ connection defaults for local config refresh testing.
- Populated `configdemo-dev.yaml` with dev build metadata and RabbitMQ defaults.

### Changed

- Exposed `busrefresh` actuator support for config refresh propagation.
- Activated the `dev` profile by default in `config-demo`.
- Updated the config server native search location to the local config directory path.

## 2026-07-14

### Added

- Added `configserver` as a Spring Cloud Config Server running on port `8888`.
- Configured the config server to use the Git backend at `https://github.com/alirezaazizollahi/app-configuration.git` with default label `master`.
- Added RSA keystore encryption configuration using `config-server.jks` and alias `config-server-key`.
- Added `docs/keytool-command.txt` with the keytool command used to generate the config server keystore.

### Changed

- Replaced the config server Git password value with the `APP_CONFIGURATION_TOKEN` environment variable.
- Moved AI-maintained markdown documentation from `docs/` to `docs/ai-docs/`.

## 2026-05-20

### Added

- Created initial project documentation under `docs/`.
- Documented service responsibilities for `user-ms`, `product-ms`, and `order-ms`.
- Documented current API endpoints, DTO examples, entities, persistence model, dependencies, and runtime configuration.
- Added decision log and open questions for future design work.

### Current First-Version Baseline

- `user-ms`
  - Runs on port `8082`.
  - Uses PostgreSQL database `userdb`.
  - Provides user list, read, create, and update endpoints.
  - Stores users, addresses, roles, and timestamps.
- `product-ms`
  - Runs on port `8083`.
  - Uses PostgreSQL database `product`.
  - Provides product create, list, read, update, soft delete, search, and failure simulation endpoints.
  - Stores product catalog data with an `active` soft-delete flag.
- `order-ms`
  - Runs on port `8084`.
  - Uses PostgreSQL database `order`.
  - Provides cart add/list/delete and order creation endpoints.
  - Stores carts, orders, order items, and order status locally.

### Known Baseline Gaps

- No API gateway.
- No service discovery.
- No active authentication or authorization.
- No implemented inter-service calls.
- No database migration tool.
- Docker Compose creates `mydb`, but service configs expect `userdb`, `product`, and `order`.
- Cart item price is hardcoded in `order-ms`.
