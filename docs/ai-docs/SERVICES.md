# Services

## Infrastructure applications

### Eureka (`eureka`)

- Entry point: `com.raalapp.EurekaApplication`, annotated with
  `@EnableEurekaServer`.
- Port: `8761`.
- Eureka server mode disables client self-registration and registry fetching.

### Config Server (`configserver`)

- Entry point: `com.raalapp.ConfigserverApplication`, annotated with
  `@EnableConfigServer`.
- Port: `8888`; native profile serves
  `configserver/src/main/resources/config`.
- Service configs are keyed by `order-service`, `product-service`, and
  `user-service`.
- Includes RabbitMQ bus support and a keystore for config encryption.

### Config demo (`config-demo`)

- A separate Spring Cloud Config client demonstration with a build-info
  controller; it is not part of the ecommerce request flow.
- Two profile files use the unusual `.yaml1` suffix and may not be loaded by
  Spring as YAML configuration.

## Ecommerce services

### User Service (`user-ms`)

- Entry point: `com.raalapp.ecommerce.UserApplication`; port `8082` is remotely
  configured.
- Owns user/address models, repository, mapper, service, and REST controller.
- Its config declares a MongoDB URI as well as JPA properties; verify intended
  persistence before treating it as operational.

### Product Service (`product-ms`)

- Entry point: `com.raalapp.ecommerce.ProductApplication`; port `8083` is
  remotely configured.
- Owns the product model, repository, mapper, service, and REST controller.
- Registers as `product-service`, the discovery name used by the order client.

### Order Service (`order-ms`)

- Entry point: `com.raalapp.ecommerce.OrderApplication`; port `8084` is remotely
  configured.
- Owns cart/order models, repositories, services, and REST controllers.
- Calls `product-service` through a Spring HTTP interface and load-balanced
  `RestClient`.
- Cart price is currently hardcoded in `CartService`; order creation clears
  the cart. See [API.md](API.md) and [DATA-MODEL.md](DATA-MODEL.md).
- See [STARTUP-TROUBLESHOOTING.md](STARTUP-TROUBLESHOOTING.md) for Eureka
  client warnings.

## Config Server

Path: `configserver`

Runtime:

- Application class: `com.raalapp.ConfigserverApplication`
- Port: `8888`
- Spring application name: `configserver`
- No local database

### Main Packages

| Package | Responsibility |
| --- | --- |
| `com.raalapp` | Spring Boot application entry point and config server enablement |

### Behavior

`ConfigserverApplication` enables Spring Cloud Config Server with `@EnableConfigServer`.

The server currently reads externalized configuration from native classpath files:

```text
configserver/src/main/resources/config
```

Current config files are `order-service.yaml`, `product-service.yaml`, and
`user-service.yaml`; each provides external settings for its matching client.

The previous Git backend settings remain commented in `configserver/src/main/resources/application.yaml` for future remote repository use.

The service is also configured for RSA-backed property encryption through `config-server.jks` with alias `config-server-key`.

## User Service

Path: `user-ms`

Runtime:

- Application class: `com.raalapp.ecommerce.UserApplication`
- Port: `8082`
- Spring application name: `user-service`
- MongoDB URI is currently configured remotely; persistence setup should be
  reconciled with the JPA entity/repository code.

### Main Packages

| Package | Responsibility |
| --- | --- |
| `controllers` | HTTP endpoints for users |
| `services` | User business logic |
| `repository` | Spring Data JPA repository |
| `models` | JPA entities and enum |
| `dto` | Request and response DTOs |
| `mapper` | MapStruct mapper |

### Controller

`UserController` exposes `/api/users`.

Current operations:

- Fetch all users.
- Fetch one user by path id.
- Create user.
- Update user.

The `GET /api/users/{id}` endpoint includes logging examples at multiple log levels.

### Service

`UserService` handles:

- Loading all users from `UserRepository`.
- Parsing string path IDs to `Long`.
- Creating users through `UserMapper`.
- Updating user scalar fields and replacing address data when present.
- Mapping `User` entities to `UserResponse`.

### Repository

`UserRepository extends JpaRepository<User, Long>`.

### Entities

- `User`
- `Address`
- `UserRole`

### DTOs

- `UserRequest`
- `UserResponse`
- `AddressDTO`

### Business Behavior

- New users default to role `CUSTOMER`.
- Address is owned by user through cascade and orphan removal.
- User timestamps are maintained by Hibernate annotations.
- Keycloak integration is planned but commented out.

## Product Service

Path: `product-ms`

Runtime:

- Application class: `com.raalapp.ecommerce.ProductApplication`
- Port: `8083`
- Spring application name: `product`
- Database URL: `jdbc:postgresql://localhost:5432/product`

### Main Packages

| Package | Responsibility |
| --- | --- |
| `controllers` | HTTP endpoints for products |
| `services` | Product business logic |
| `repositories` | Spring Data JPA repository |
| `models` | JPA entity |
| `dtos` | Request and response DTOs |
| `mapper` | MapStruct mapper |

### Controller

`ProductController` exposes `/api/products`.

Current operations:

- Create product.
- List active products.
- Fetch active product by id.
- Update product.
- Soft delete product.
- Search active in-stock products by name keyword.
- Simulate failure for testing.

### Service

`ProductService` handles:

- Creating products.
- Updating product fields from requests.
- Returning only active products for listing.
- Soft deleting products by setting `active = false`.
- Searching active products with stock greater than zero.
- Mapping `Product` entities to `ProductResponse`.

### Repository

`ProductRepository extends JpaRepository<Product, Long>`.

Custom methods:

- `findByActiveTrue()`
- `findByIdAndActiveTrue(Long id)`
- `searchProducts(String keyword)`

### Entity

- `Product`

### DTOs

- `ProductRequest`
- `ProductResponse`

### Business Behavior

- Products default to `active = true`.
- Delete is a soft delete.
- Search only returns active products with `stockQuantity > 0`.
- MapStruct is configured, but product mapping is currently manual in the service.

## Order Service

Path: `order-ms`

Runtime:

- Application class: `com.raalapp.ecommerce.OrderApplication`
- Port: `8084`
- Spring application name: `order-ms`
- Database URL: `jdbc:postgresql://localhost:5432/order`

### Main Packages

| Package | Responsibility |
| --- | --- |
| `controller` | HTTP endpoints for cart and orders |
| `services` | Cart and order business logic |
| `repositories` | Spring Data JPA repositories |
| `models` | JPA entities and order status enum |
| `dtos` | Request and response DTOs, plus copied user/product DTO shapes |

### Controllers

`CartController` exposes `/api/cart`.

Current operations:

- Add item to cart.
- Delete item from cart by product id.
- Fetch cart by user id header.

`OrderController` exposes `/api/orders`.

Current operations:

- Create an order from the current cart for a user id header.

### Services

`CartService` handles:

- Adding a new cart item.
- Increasing quantity when the same user/product pair already exists.
- Assigning a hardcoded item price of `1000.00`.
- Removing a cart item.
- Fetching cart items by user.
- Clearing all cart items for a user.

`OrderService` handles:

- Reading cart items for a user.
- Returning empty when cart has no items.
- Creating an order with status `CONFIRMED`.
- Copying cart items into order items.
- Calculating total amount from cart item price values.
- Clearing the cart after order persistence.
- Mapping orders to `OrderResponse`.

### Repositories

`CartItemRepository extends JpaRepository<CartItem, Long>`.

Custom methods:

- `findByUserIdAndProductId(Long userId, Long productId)`
- `deleteByUserIdAndProductId(Long userId, Long productId)`
- `findByUserId(Long userId)`
- `deleteByUserId(Long userId)`

`OrderRepository extends JpaRepository<Order, Long>`.

### Entities

- `CartItem`
- `Order`
- `OrderItem`
- `OrderStatus`

### DTOs

- `CartItemRequest`
- `OrderResponse`
- `OrderItemDTO`
- `ProductResponse`
- `UserResponse`
- `AddressDTO`
- `UserRole`

### Business Behavior

- Order service currently stores external references as IDs only.
- Cart and orders do not have JPA relationships to user or product services.
- Cart price is fixed and does not come from product service.
- Order total currently sums item price values only; it does not multiply by quantity for the stored total.
- `OrderItemDTO.subTotal` does multiply price by quantity in the response.
