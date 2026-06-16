# OpenCommerce System Architecture v1.0

## Overview

OpenCommerce is an open-source e-commerce platform built using Java microservices, React, MySQL, and Kafka. The architecture is designed to be modular, scalable, and easy for developers to clone, customize, and deploy for their own stores.

The system uses:

* React frontend for customer and admin experiences
* API Gateway for routing and security enforcement
* Microservices for business capabilities
* MySQL database per service
* Kafka for asynchronous communication
* Docker Compose for local development
* AWS for deployment

---

## Architecture Goals

* Separate business capabilities into independently deployable services
* Allow developers to extend the project without changing core code
* Keep services loosely coupled through REST and Kafka events
* Support open-source contributors with clear contracts and documentation
* Provide a production-style structure that is realistic for a solo developer

---

# 1. High-Level System View

```text
Customer Browser / Admin Browser
            |
            v
        React UI
            |
            v
        API Gateway
            |
   -----------------------------
   |            |              |
   v            v              v
Auth Service  Catalog Service  Cart Service
                   |              |
                   v              v
              Order Service   Address APIs
                   |
                   v
                Kafka
                   |
                   v
          Notification Service
```

---

# 2. C4 Context Diagram

## Actors

### Customer

* Registers and logs in
* Browses products
* Adds items to cart
* Places orders
* Views order history

### Administrator

* Manages catalog data
* Manages inventory
* Views all orders
* Updates order status

### External Systems

* Kafka for event-driven communication
* AWS S3 for product images in deployment phase
* Email provider in future releases

---

# 3. Container Diagram

## Frontend Containers

### Customer Store UI

A React application used by customers to browse products, manage carts, and place orders.

### Admin Dashboard UI

A React application used by administrators to manage categories, products, and orders.

---

## Backend Containers

### API Gateway

Responsible for:

* Request routing
* JWT validation
* Role-based access enforcement
* Centralized cross-cutting concerns

### Auth Service

Responsible for:

* User registration
* Login
* Token issuance
* Profile retrieval
* Role management

### Catalog Service

Responsible for:

* Category management
* Product management
* Product images and attributes
* Inventory updates

### Cart Service

Responsible for:

* Cart creation and retrieval
* Add, update, remove cart items
* Cart totals

### Order Service

Responsible for:

* Checkout
* Order creation
* Order history
* Order status updates
* Publishing Kafka events

### Notification Service

Responsible for:

* Consuming order events from Kafka
* Sending order confirmation notifications
* Future support for additional event-driven notifications

---

# 4. Communication Model

OpenCommerce uses two communication styles:

## Synchronous Communication

Used for request/response operations.

Examples:

* Login request to Auth Service
* Product listing request to Catalog Service
* Cart retrieval request to Cart Service
* Order placement request to Order Service

## Asynchronous Communication

Used for decoupled event processing.

Examples:

* Order Service publishes `order-created`
* Order Service publishes `order-status-updated`
* Catalog Service publishes `product-created`
* Catalog Service publishes `product-updated`
* Catalog Service publishes `inventory-updated`

Consumers listen to Kafka topics independently.

---

# 5. Service Responsibilities

## Auth Service

Handles identity and access control.

Main responsibilities:

* Register users
* Authenticate users
* Issue JWT access tokens
* Refresh tokens
* Return current authenticated user profile

Database:

* auth_db

---

## Catalog Service

Handles the product catalog and inventory data.

Main responsibilities:

* Manage categories
* Manage products
* Manage product images
* Store product attributes
* Update stock quantity

Database:

* catalog_db

---

## Cart Service

Handles shopping cart state.

Main responsibilities:

* Create cart for user
* Add items to cart
* Update item quantity
* Remove items from cart
* Return cart summary

Database:

* cart_db

---

## Order Service

Handles checkout and order lifecycle.

Main responsibilities:

* Create order from cart
* Store order snapshots
* Update order status
* Return order history
* Publish Kafka events after important changes

Database:

* order_db

---

## Notification Service

Handles event-driven notifications.

Main responsibilities:

* Consume order-related Kafka events
* Send order confirmation notifications
* Send order status update notifications

Database:

* Optional for v1.0

---

# 6. Request Flow Diagrams

## User Registration Flow

```text
React UI
  -> API Gateway
  -> Auth Service
  -> auth_db
  -> Response to UI
```

## Product Browsing Flow

```text
React UI
  -> API Gateway
  -> Catalog Service
  -> catalog_db
  -> Response to UI
```

## Add to Cart Flow

```text
React UI
  -> API Gateway
  -> Cart Service
  -> cart_db
  -> Response to UI
```

## Checkout Flow

```text
React UI
  -> API Gateway
  -> Order Service
  -> order_db
  -> Kafka order-created event
  -> Notification Service
```

## Order Status Update Flow

```text
Admin UI
  -> API Gateway
  -> Order Service
  -> order_db
  -> Kafka order-status-updated event
  -> Notification Service
```

---

# 7. Security Architecture

## Authentication

JWT-based authentication is used for all protected routes.

### Flow

1. User logs in with email and password.
2. Auth Service validates credentials.
3. Auth Service issues JWT access token.
4. Frontend stores token securely.
5. Frontend sends token in Authorization header.
6. Gateway validates token before forwarding requests.

## Authorization

Role-based access control is used.

Roles:

* ROLE_ADMIN
* ROLE_CUSTOMER

Example access rules:

* Admin APIs require ROLE_ADMIN
* Customer APIs require ROLE_CUSTOMER or authenticated access

## Password Security

* Passwords are hashed using BCrypt
* Plaintext passwords are never stored

---

# 8. Data Ownership Model

Each service owns its own database and schema.

## Ownership Rules

* Auth Service owns user identity data
* Catalog Service owns product and category data
* Cart Service owns cart data
* Order Service owns order data
* No direct table sharing between services

## Cross-Service Data Usage

When one service needs data from another service:

* Use REST API for synchronous lookup
* Use Kafka events for state propagation
* Store snapshots where historical accuracy is required

Examples:

* Order items store product name and price snapshot
* Order addresses store order-time shipping snapshot

---

# 9. Kafka Event Flow

## Order Created

1. Order Service creates order
2. Order Service publishes `order-created`
3. Notification Service consumes the event
4. Notification Service sends confirmation notification

## Order Status Updated

1. Admin updates order status
2. Order Service saves updated status
3. Order Service publishes `order-status-updated`
4. Notification Service consumes the event
5. Notification Service sends status update notification

## Product Created / Updated

1. Catalog Service saves product data
2. Catalog Service publishes product event
3. Future services can consume the event for search, analytics, or recommendations

---

# 10. Deployment Architecture

## Local Development

The entire platform should run locally using Docker Compose.

Services:

* MySQL containers
* Kafka containers
* Auth Service
* Catalog Service
* Cart Service
* Order Service
* Notification Service
* API Gateway
* React frontend

Startup command:

```bash
docker compose up
```

---

## AWS Deployment

For cloud deployment, use:

* EC2 for application containers
* RDS MySQL for databases
* S3 for product image storage
* ECR for Docker images

Recommended flow:

```text
GitHub -> GitHub Actions -> Docker Image Build -> ECR -> EC2 Deployment
```

---

# 11. Recommended Repository Structure

```text
opencommerce

backend/
  api-gateway/
  auth-service/
  catalog-service/
  cart-service/
  order-service/
  notification-service/
  discovery-service/

frontend/
  customer-ui/
  admin-ui/

docs/
  requirements.md
  database-design.md
  api-contracts.md
  kafka-event-contracts.md
  system-architecture.md

docker/
.github/
README.md
LICENSE
CONTRIBUTING.md
```

---

# 12. Development Principles

* Build the minimum usable version first
* Keep service boundaries clear
* Store snapshots where historical correctness matters
* Prefer explicit contracts over hidden assumptions
* Use one service per bounded context
* Add new capabilities as future modules, not core complexity

---

# 13. Version 1.0 Scope

Included:

* Auth
* Categories
* Products
* Cart
* Orders
* Kafka notifications
* Admin dashboard
* Docker Compose setup

Excluded:

* Payments
* Wishlist
* Reviews
* Coupons
* Recommendation engine
* Multi-tenant support

---

# 14. Future Expansion Plan

Possible v2+ modules:

* Payment Service
* Inventory Service
* Wishlist Service
* Review Service
* Search Service
* Recommendation Service
* Vendor Service
* Store Settings Service

These should be added only after v1.0 is stable and documented.
