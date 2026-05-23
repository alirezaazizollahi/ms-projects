# Changes

Use this file as the human-readable change log for the project. Add entries for code, configuration, database, API, dependency, and documentation changes.

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

