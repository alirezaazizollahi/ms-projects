# Services

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

The server reads externalized configuration from:

```text
https://github.com/alirezaazizollahi/app-configuration.git
```

The Git backend uses:

- Default label: `master`
- Username: `alirezaazizollahi`
- Password token source: `APP_CONFIGURATION_TOKEN`

The service is also configured for RSA-backed property encryption through `config-server.jks` with alias `config-server-key`.

## User Service

Path: `user-ms`

Runtime:

- Application class: `com.raalapp.ecommerce.UserApplication`
- Port: `8082`
- Spring application name: `user-ms`
- Database URL: `jdbc:postgresql://localhost:5432/userdb`

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
