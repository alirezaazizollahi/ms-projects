# Configuration

## Runtime Ports

| Service | Port | Config File |
| --- | ---: | --- |
| Config Server | `8888` | `configserver/src/main/resources/application.yaml` |
| Eureka Server | `8761` | `eureka/src/main/resources/application.yaml` |
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

Native backend:

```yaml
spring:
  profiles:
    active: native
  cloud:
    config:
      server:
        health:
          enabled: false
        native:
          search-locations: file:///home/alireza/projects/my-projects/microservices/embarkX/ms-projects/configserver/src/main/resources/config
```

Native config files:

- `configserver/src/main/resources/config/order-service.yaml`: order service port, PostgreSQL, RabbitMQ, actuator, and Eureka settings.
- `configserver/src/main/resources/config/product-service.yaml`: product service port, PostgreSQL, RabbitMQ, actuator, and Eureka settings.
- `configserver/src/main/resources/config/user-service.yaml`: user service port, MongoDB, JPA, RabbitMQ, actuator, and Eureka settings.

`config-demo` has local configuration files in its own resources directory; the
files ending in `yaml1` are not standard Spring YAML filenames and are not
assumed to be active profiles.

The previous Git backend block is currently commented in `configserver/src/main/resources/application.yaml`. Re-enabling Git-backed configuration would require restoring that backend and providing `APP_CONFIGURATION_TOKEN`.

Config refresh:

- `configserver` and `config-demo` include Spring Cloud Bus AMQP support.
- Both services use RabbitMQ at `localhost:5672` with `guest` / `guest`.
- `configserver` exposes the actuator `busrefresh` endpoint.
- `config-demo` exposes actuator `refresh` and `busrefresh` endpoints.
- `config-demo` activates the `dev` profile by default, so it receives `configdemo-dev.yaml` overrides from the config server.

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

The checked-in remote configs use `${DB_USER}` / `${DB_PASSWORD}` for the
PostgreSQL services. The user service config currently uses MongoDB for its URI
and also declares JPA settings; confirm intended persistence before relying on
that combination. Do not infer active credentials from old documentation.

| Service | JDBC URL | Database Name |
| --- | --- | --- |
| User Service | Mongo URI in `user-service.yaml` | `userdb` |
| Product Service | `jdbc:postgresql://localhost:5432/products` | `products` |
| Order Service | `jdbc:postgresql://localhost:5432/orders` | `orders` |

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

No Docker Compose file is currently present in this repository. Start/configure
PostgreSQL, MongoDB, and RabbitMQ separately for local runs.

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
- `org.springframework.cloud:spring-cloud-starter-bus-amqp`
- `org.springframework.boot:spring-boot-starter-actuator`
- Spring Cloud dependency BOM `2025.1.2`

Config demo dependencies:

- `org.springframework.cloud:spring-cloud-starter-bus-amqp`

Additional mapping dependency:

- `org.mapstruct:mapstruct:1.4.2.Final`
- `org.mapstruct:mapstruct-processor:1.4.2.Final` configured through Maven compiler annotation processors

MapStruct is declared in `user-ms` and `product-ms`. `order-ms` configures the MapStruct processor path but does not declare a direct MapStruct dependency in the dependencies section.

## Maven Repositories

All three Maven projects configure these repositories and plugin repositories:

- `https://mvnhub.ir/`
- `https://mirror-maven.runflare.com/maven2`
