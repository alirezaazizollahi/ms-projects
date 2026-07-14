# Data Model

## User Service

### `User`

JPA entity name: `users`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `keycloakId` | `String` | Stored but not currently populated by active code |
| `firstName` | `String` | User profile field |
| `lastName` | `String` | User profile field |
| `email` | `String` | User profile field |
| `phone` | `String` | User profile field |
| `role` | `UserRole` | Enum stored as string, defaults to `CUSTOMER` |
| `address` | `Address` | One-to-one, cascade all, orphan removal |
| `createdAt` | `LocalDateTime` | Hibernate creation timestamp |
| `updatedAt` | `LocalDateTime` | Hibernate update timestamp |

### `Address`

Table: `addresses`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `street` | `String` | Address line |
| `city` | `String` | City |
| `state` | `String` | State/province |
| `country` | `String` | Country |
| `zipcode` | `String` | Postal code |

### `UserRole`

Values:

- `CUSTOMER`
- `ADMIN`

## Product Service

### `Product`

JPA entity name: `products`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `name` | `String` | Product name |
| `description` | `String` | Product description |
| `price` | `BigDecimal` | Product price |
| `stockQuantity` | `Integer` | Stock quantity |
| `category` | `String` | Product category |
| `imageUrl` | `String` | Product image URL |
| `active` | `Boolean` | Defaults to `true`; used for soft delete |
| `createdAt` | `LocalDateTime` | Hibernate creation timestamp |
| `updatedAt` | `LocalDateTime` | Hibernate update timestamp |

## Order Service

### `CartItem`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `userId` | `Long` | External reference to user id |
| `productId` | `Long` | External reference to product id |
| `quantity` | `Integer` | Quantity in cart |
| `price` | `BigDecimal` | Currently hardcoded at add-to-cart time |
| `createdAt` | `LocalDateTime` | Hibernate creation timestamp |
| `updatedAt` | `LocalDateTime` | Hibernate update timestamp |

### `Order`

JPA entity name: `orders`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `userId` | `Long` | External reference to user id |
| `totalAmount` | `BigDecimal` | Currently sum of cart item prices |
| `status` | `OrderStatus` | Enum stored as string, defaults to `PENDING`; create order sets `CONFIRMED` |
| `items` | `List<OrderItem>` | One-to-many, cascade all, orphan removal |
| `createdAt` | `LocalDateTime` | Hibernate creation timestamp |
| `updatedAt` | `LocalDateTime` | Hibernate update timestamp |

### `OrderItem`

| Field | Type | Notes |
| --- | --- | --- |
| `id` | `Long` | Primary key, identity generated |
| `productId` | `Long` | External reference to product id |
| `quantity` | `Integer` | Ordered quantity |
| `price` | `BigDecimal` | Price copied from cart item |
| `order` | `Order` | Many-to-one relationship through `order_id` |

### `OrderStatus`

Values:

- `PENDING`
- `CONFIRMED`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

## Cross-Service Data

The first version does not use distributed joins or shared database tables. Cross-service references are stored as primitive IDs:

- `order-ms.CartItem.userId` refers to a user owned by `user-ms`.
- `order-ms.CartItem.productId` refers to a product owned by `product-ms`.
- `order-ms.Order.userId` refers to a user owned by `user-ms`.
- `order-ms.OrderItem.productId` refers to a product owned by `product-ms`.

