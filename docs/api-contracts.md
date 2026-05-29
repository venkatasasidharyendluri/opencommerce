# OpenCommerce API Contracts v1.0

## API Standards

### Base URL

```http
/api/v1
```

### Authentication

Protected endpoints require:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Content Type

```http
Content-Type: application/json
```

---

# Standard Success Response

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {}
}
```

---

# Standard Error Response

```json
{
  "success": false,
  "timestamp": "2026-05-29T10:00:00Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Email is required",
  "path": "/api/v1/auth/register"
}
```

---

# AUTH SERVICE

Base Path

```http
/api/v1/auth
```

---

## Register User

### Endpoint

```http
POST /api/v1/auth/register
```

### Request

```json
{
  "firstName": "Sasi",
  "lastName": "Chowdary",
  "email": "sasi@gmail.com",
  "password": "Password@123"
}
```

### Response

```json
{
  "success": true,
  "message": "User registered successfully"
}
```

### Status Codes

```text
201 CREATED
400 BAD REQUEST
409 CONFLICT
```

---

## Login

### Endpoint

```http
POST /api/v1/auth/login
```

### Request

```json
{
  "email": "sasi@gmail.com",
  "password": "Password@123"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "accessToken": "jwt-token",
    "refreshToken": "refresh-token",
    "tokenType": "Bearer",
    "expiresIn": 3600
  }
}
```

### Status Codes

```text
200 OK
401 UNAUTHORIZED
```

---

## Get Current User

### Endpoint

```http
GET /api/v1/auth/me
```

### Response

```json
{
  "id": 1,
  "uuid": "123e4567",
  "firstName": "Sasi",
  "lastName": "Chowdary",
  "email": "sasi@gmail.com",
  "roles": [
    "ROLE_CUSTOMER"
  ]
}
```

---

# CATEGORY API

Base Path

```http
/api/v1/categories
```

---

## Get Categories

```http
GET /api/v1/categories
```

### Response

```json
[
  {
    "id": 1,
    "name": "Electronics",
    "slug": "electronics"
  }
]
```

---

## Create Category

ROLE_ADMIN

```http
POST /api/v1/categories
```

### Request

```json
{
  "name": "Electronics",
  "description": "Electronic Products",
  "parentCategoryId": null
}
```

### Response

```json
{
  "id": 1,
  "name": "Electronics"
}
```

---

## Update Category

```http
PUT /api/v1/categories/{id}
```

---

## Delete Category

```http
DELETE /api/v1/categories/{id}
```

---

# PRODUCT API

Base Path

```http
/api/v1/products
```

---

## Get Products

```http
GET /api/v1/products
```

### Query Parameters

```text
?page=0
&size=20
&sort=name,asc
&category=electronics
&search=laptop
```

### Response

```json
{
  "content": [
    {
      "id": 1,
      "name": "MacBook Air",
      "price": 99999,
      "stockQuantity": 15,
      "imageUrl": "https://..."
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 100
}
```

---

## Get Product By Id

```http
GET /api/v1/products/{id}
```

### Response

```json
{
  "id": 1,
  "name": "MacBook Air",
  "description": "Laptop",
  "price": 99999,
  "stockQuantity": 10,
  "images": [],
  "attributes": []
}
```

---

## Create Product

ROLE_ADMIN

```http
POST /api/v1/products
```

### Request

```json
{
  "categoryId": 1,
  "sku": "MAC-AIR-M3",
  "name": "MacBook Air",
  "shortDescription": "Apple Laptop",
  "description": "Detailed Description",
  "price": 99999,
  "stockQuantity": 50
}
```

### Response

```json
{
  "id": 1,
  "message": "Product created successfully"
}
```

---

## Update Product

```http
PUT /api/v1/products/{id}
```

---

## Delete Product

```http
DELETE /api/v1/products/{id}
```

---

# CART API

Base Path

```http
/api/v1/cart
```

Requires Authentication

---

## Get Cart

```http
GET /api/v1/cart
```

### Response

```json
{
  "cartId": 1,
  "items": [
    {
      "productId": 1,
      "productName": "MacBook Air",
      "quantity": 2,
      "unitPrice": 99999
    }
  ],
  "totalAmount": 199998
}
```

---

## Add Item To Cart

```http
POST /api/v1/cart/items
```

### Request

```json
{
  "productId": 1,
  "quantity": 2
}
```

### Response

```json
{
  "message": "Item added to cart"
}
```

---

## Update Cart Item

```http
PUT /api/v1/cart/items/{itemId}
```

### Request

```json
{
  "quantity": 5
}
```

---

## Remove Cart Item

```http
DELETE /api/v1/cart/items/{itemId}
```

---

# ADDRESS API

Base Path

```http
/api/v1/addresses
```

---

## Get User Addresses

```http
GET /api/v1/addresses
```

---

## Create Address

```http
POST /api/v1/addresses
```

### Request

```json
{
  "fullName": "Sasi Chowdary",
  "phone": "9876543210",
  "addressLine1": "Street 1",
  "addressLine2": "Near Park",
  "city": "Hyderabad",
  "state": "Telangana",
  "postalCode": "500001",
  "country": "India"
}
```

---

# ORDER API

Base Path

```http
/api/v1/orders
```

---

## Place Order

```http
POST /api/v1/orders
```

### Request

```json
{
  "addressId": 1
}
```

### Response

```json
{
  "orderId": 1001,
  "orderNumber": "ORD-20260529-1001",
  "status": "PENDING"
}
```

---

## Get My Orders

```http
GET /api/v1/orders
```

### Response

```json
[
  {
    "orderId": 1001,
    "orderNumber": "ORD-20260529-1001",
    "status": "SHIPPED",
    "totalAmount": 99999
  }
]
```

---

## Get Order Details

```http
GET /api/v1/orders/{id}
```

---

# ADMIN ORDER API

ROLE_ADMIN

---

## Get All Orders

```http
GET /api/v1/admin/orders
```

### Query Parameters

```text
?page=0
&size=20
&status=PENDING
```

---

## Update Order Status

```http
PATCH /api/v1/admin/orders/{id}/status
```

### Request

```json
{
  "status": "SHIPPED"
}
```

Allowed Values:

* PENDING
* CONFIRMED
* PAID
* SHIPPED
* DELIVERED
* CANCELLED
* REFUNDED

---

# Kafka Event Contracts

## Topic

```text
order-created
```

### Payload

```json
{
  "orderId": 1001,
  "orderNumber": "ORD-20260529-1001",
  "userId": 10,
  "email": "user@gmail.com",
  "totalAmount": 99999,
  "createdAt": "2026-05-29T10:00:00Z"
}
```

Consumed By:

* Notification Service

---

# API Versioning Strategy

Current Version:

```text
v1
```

Example:

```http
/api/v1/products
```

Future versions:

```http
/api/v2/products
```
