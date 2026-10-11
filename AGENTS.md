# Repository guidance

## Scope

This repository contains independent Maven projects rather than one root Maven
build. Check the affected service's `pom.xml`, source, and configuration before
making changes. Do not assume settings in one service apply to another.

## Working agreements

- Keep changes scoped to the service or infrastructure component being worked
  on. Preserve unrelated working-tree changes.
- Treat `configserver/src/main/resources/config/*.yaml` as runtime configuration
  consumed by services through Spring Cloud Config. Keep the service name in
  each file aligned with `spring.application.name`.
- Keep `docs/ai-docs` aligned with the repository. Distinguish verified source
  configuration from intended behavior and runtime observations.
- Do not commit secrets, credentials, keystores, or generated build output.
- For Java changes, use that service's Maven wrapper and report which checks
  were run. Do not claim a service is operational based only on compilation.

## Repository map

- `eureka/`: Eureka discovery server.
- `configserver/`: native Spring Cloud Config Server and per-service config.
- `config-demo/`: standalone Config Server client demonstration.
- `user-ms/`, `product-ms/`, `order-ms/`: ecommerce APIs.
- `docs/ai-docs/`: maintained architecture, service, API, and development docs.
