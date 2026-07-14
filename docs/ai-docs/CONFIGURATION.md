# Configuration

## Runtime Ports

| Service | Port | Config File |
| --- | ---: | --- |
| Config Server | `8888` | `configserver/src/main/resources/application.yaml` |
| User Service | `8082` | `user-ms/src/main/resources/application.yaml` |
| Product Service | `8083` | `product-ms/src/main/resources/application.yaml` |
| Order Service | `8084` | `order-ms/src/main/resources/application.yaml` |
| pgAdmin | `5050` | `files/docker-compose-postgres-pagadmin.yml` |
| PostgreSQL | `5432` | `files/docker-compose-postgres-pagadmin.yml` |

## Config Server

Path: `configserver`

Runtime:

- Spring application name: `configserver`
- Port: `8888`
- Main class: `com.raalapp.ConfigserverApplication`
- Spring Cloud Config Server is enabled with `@EnableConfigServer`

Git backend:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/alirezaazizollahi/app-configuration.git
          default-label: master
          username: alirezaazizollahi
          password: ${APP_CONFIGURATION_TOKEN}
```

Required environment:

- `APP_CONFIGURATION_TOKEN`: token used by the config server to access the Git-backed configuration repository.

Encryption:

```yaml
encrypt:
  key-store:
    location: config-server.jks
    password: changeit
    alias: config-server-key
```

The keystore file is currently stored at `configserver/src/main/resources/config-server.jks`. The helper command used to generate it is documented in `docs/keytool-command.txt`.

## Database Configuration

All services use PostgreSQL with username `admin` and password `admin123`.

| Service | JDBC URL | Database Name |
| --- | --- | --- |
| User Service | `jdbc:postgresql://localhost:5432/userdb` | `userdb` |
| Product Service | `jdbc:postgresql://localhost:5432/product` | `product` |
| Order Service | `jdbc:postgresql://localhost:5432/order` | `order` |

JPA settings currently used:

- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true`
- `hibernate.format_sql=true`
- PostgreSQL dialect configured explicitly

`user-ms` and `order-ms` also enable SQL init mode:

```yaml
spring:
  sql:
    init:
      mode: always
```

## Docker Compose

Infrastructure file: `files/docker-compose-postgres-pagadmin.yml`

Services:

- `postgres`
  - Image: `postgres:14.22-trixie`
  - Container name: `postgres-db`
  - Exposes `5432:5432`
  - Default database: `mydb`
- `pgadmin`
  - Image: `dpage/pgadmin4:latest`
  - Container name: `pgadmin`
  - Exposes `5050:80`
  - Login: `admin@example.com` / `admin123`

Important: the Compose file creates default database `mydb`, while the services expect `userdb`, `product`, and `order`. Create those databases manually in PostgreSQL or update the infrastructure setup before starting all services.

## Maven and Dependencies

Each service uses its own Maven Wrapper.

Common service dependencies:

- `org.springframework.boot:spring-boot-starter-parent:4.0.6`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-webmvc`
- `org.postgresql:postgresql`
- `org.projectlombok:lombok`
- `spring-boot-starter-data-jpa-test`
- `spring-boot-starter-webmvc-test`

Config server dependencies:

- `org.springframework.boot:spring-boot-starter-parent:4.1.0`
- `org.springframework.cloud:spring-cloud-config-server`
- Spring Cloud dependency BOM `2025.1.2`

Additional mapping dependency:

- `org.mapstruct:mapstruct:1.4.2.Final`
- `org.mapstruct:mapstruct-processor:1.4.2.Final` configured through Maven compiler annotation processors

MapStruct is declared in `user-ms` and `product-ms`. `order-ms` configures the MapStruct processor path but does not declare a direct MapStruct dependency in the dependencies section.

## Maven Repositories

All three Maven projects configure these repositories and plugin repositories:

- `https://mvnhub.ir/`
- `https://mirror-maven.runflare.com/maven2`
