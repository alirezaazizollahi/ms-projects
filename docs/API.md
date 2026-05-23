# API Reference

This file describes the current HTTP API in the first project version.

## User Service

Base URL locally: `http://localhost:8082`

### `GET /api/users`

Returns all users.

Response: `200 OK`

```json
[
  {
    "id": 1,
    "keyCloakId": null,
    "firstName": "Ali",
    "lastName": "Example",
    "email": "ali@example.com",
    "phone": "09120000000",
    "role": "CUSTOMER",
    "address": {
      "street": "Street 1",
      "city": "Tehran",
      "state": "Tehran",
      "country": "Iran",
      "zipcode": "12345"
    }
  }
]
```

### `GET /api/users/{id}`

Returns one user by numeric id.

Responses:

- `200 OK` with `UserResponse`
- `404 Not Found` when id is invalid or user does not exist

### `POST /api/users`

Creates a user.

Request:

```json
{
  "username": "ali",
  "firstName": "Ali",
  "lastName": "Example",
  "password": "secret",
  "email": "ali@example.com",
  "phone": "09120000000",
  "address": {
    "street": "Street 1",
    "city": "Tehran",
    "state": "Tehran",
    "country": "Iran",
    "zipcode": "12345"
  }
}
```

Response: `200 OK`

```text
User added successfully
```

### `PUT /api/users/{id}`

Updates a user by numeric id.

Responses:

- `200 OK` with text `User updated successfully`
- `404 Not Found` when id is invalid or user does not exist

## Product Service

Base URL locally: `http://localhost:8083`

### `POST /api/products`

Creates a product.

Request:

```json
{
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "price": 120.50,
  "stockQuantity": 15,
  "category": "Accessories",
  "imageUrl": "https://example.com/keyboard.png"
}
```

Response: `201 Created` with `ProductResponse`.

### `GET /api/products`

Returns all active products.

Response: `200 OK`

### `GET /api/products/{id}`

Returns one active product by id.

Responses:

- `200 OK` with `ProductResponse`
- `404 Not Found` when product does not exist or is inactive

Important: this endpoint converts path id with `Long.valueOf(id)`, so non-numeric ids currently raise an exception instead of returning `404`.

### `PUT /api/products/{id}`

Updates a product by numeric id.

Responses:

- `200 OK` with updated `ProductResponse`
- `404 Not Found` when product does not exist

### `DELETE /api/products/{id}`

Soft deletes a product by setting `active = false`.

Responses:

- `204 No Content`
- `404 Not Found`

### `GET /api/products/search?keyword={keyword}`

Searches active products where stock quantity is greater than zero and name contains the keyword.

Response: `200 OK`

### `GET /api/products/simulate?fail={true|false}`

Testing endpoint.

Behavior:

- `fail=false` returns `200 OK` with `Product Service is OK`.
- `fail=true` throws `RuntimeException`.

## Order Service

Base URL locally: `http://localhost:8084`

### `POST /api/cart`

Adds an item to the user's cart.

Request:

```json
{
  "productId": 1,
  "userId": 1,
  "quantity": 2
}
```

Responses:

- `201 Created` with empty body when successful
- `400 Bad Request` with text `Not able to complete the request` when service returns false

Current behavior:

- Existing user/product cart item quantity is increased.
- New cart item is created when none exists.
- Price is hardcoded to `1000.00`.

### `GET /api/cart`

Returns cart items for a user.

Required header:

```text
X-User-ID: 1
```

Response: `200 OK` with list of `CartItem` entities.

### `DELETE /api/cart/items/{productId}`

Deletes one cart item for the user/product pair.

Required header:

```text
X-User-ID: 1
```

Responses:

- `204 No Content`
- `404 Not Found`

### `POST /api/orders`

Creates an order from the user's current cart.

Required header:

```text
X-User-ID: 1
```

Responses:

- `201 Created` with `OrderResponse`
- `400 Bad Request` when the cart is empty

Current behavior:

- Order status is set to `CONFIRMED`.
- Cart is cleared after order is saved.

