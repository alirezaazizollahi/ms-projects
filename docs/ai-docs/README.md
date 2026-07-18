# EmbarkX Microservices Documentation

This folder documents the first version of the EmbarkX ecommerce microservice project.
Keep these files updated with every meaningful change so developers and AI assistants can understand the system quickly.

## Project Summary

EmbarkX is currently a Spring Boot based ecommerce backend split into independent Maven services plus a Spring Cloud Config Server:

| Service | Path | Runtime Port | Database | Main Responsibility |
| --- | --- | ---: | --- | --- |
| Config Server | `configserver` | `8888` | n/a | Centralized external configuration from native classpath files and encrypted property support |
| User Service | `user-ms` | `8082` | `userdb` | User profiles, roles, and addresses |
| Product Service | `product-ms` | `8083` | `product` | Product catalog, stock fields, soft delete, search |
| Order Service | `order-ms` | `8084` | `order` | Cart items and order creation |

The ecommerce services currently share package naming under `com.raalapp.ecommerce`, but each service is its own Maven project with its own application entry point, database configuration, model classes, repositories, controllers, and service layer. The config server uses package `com.raalapp` and currently serves native configuration files from `configserver/src/main/resources/config`.

## Documentation Map

| File | Purpose |
| --- | --- |
| [UNDERSTANDING.md](UNDERSTANDING.md) | Fast mental model of the repository, architecture, service responsibilities, and current limitations |
| [SERVICES.md](SERVICES.md) | Detailed notes for each microservice: controllers, services, repositories, DTOs, entities, and business behavior |
| [API.md](API.md) | Current HTTP endpoints and request/response behavior |
| [DATA-MODEL.md](DATA-MODEL.md) | Entity and persistence model for all services |
| [CONFIGURATION.md](CONFIGURATION.md) | Ports, databases, Docker Compose, Maven dependencies, and runtime configuration |
| [DEVELOPMENT.md](DEVELOPMENT.md) | Local development commands, startup order, testing, and documentation workflow |
| [DECISIONS.md](DECISIONS.md) | Architectural and implementation decisions made so far |
| [CHANGES.md](CHANGES.md) | Human-readable change log for future project evolution |
| [OPEN-QUESTIONS.md](OPEN-QUESTIONS.md) | Known gaps and decisions still waiting for design or implementation |

## Update Rule

When changing the project:

1. Update [CHANGES.md](CHANGES.md) with what changed and why.
2. Update [DECISIONS.md](DECISIONS.md) when an architecture or design choice is made.
3. Update service/API/data/config docs when code behavior changes.
4. Keep examples and ports aligned with `application.yaml` files.
