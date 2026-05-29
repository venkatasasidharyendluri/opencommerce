# OpenCommerce Requirements

## Project Overview

OpenCommerce is an open-source e-commerce platform inspired by Amazon and Flipkart. The platform allows customers to browse products, manage carts, place orders, and allows administrators to manage products, categories, inventory, and orders.

## Goals

* Demonstrate Java Microservices Architecture.
* Provide a reusable e-commerce platform for developers.
* Support Docker-based deployment.
* Serve as a portfolio-quality open-source project.

---

# User Roles

## Customer

Customers can:

* Register an account.
* Login using email and password.
* View their profile.
* Browse products.
* Search products.
* Filter products by category.
* View product details.
* Add products to cart.
* Update cart quantities.
* Remove products from cart.
* Place orders.
* View order history.
* Rate Products.

## Administrator

Administrators can:

* Login to the admin dashboard.
* Manage categories.
* Create products.
* Update products.
* Delete products.
* Manage inventory.
* View customer orders.
* Update order status.

---

# Functional Requirements

## Authentication

### Registration

The system shall allow users to create accounts.

### Login

The system shall authenticate users using email and password.

### Authorization

The system shall support:

* ROLE_ADMIN
* ROLE_CUSTOMER

### JWT

The system shall issue JWT access tokens after successful login.

---

## Product Management

### Categories

The system shall allow administrators to:

* Create categories.
* Update categories.
* Delete categories.

### Products

The system shall allow administrators to:

* Create products.
* Update products.
* Delete products.

Product information shall include:

* Name
* Description
* Price
* Stock Quantity
* Category
* Product Images

---

## Product Browsing

The system shall allow customers to:

* View product listings.
* View product details.
* Search products.
* Filter products by category.

---

## Cart Management

The system shall allow customers to:

* Add products to cart.
* Remove products from cart.
* Update quantities.
* View cart contents.

---

## Order Management

The system shall allow customers to:

* Place orders.
* View order history.

The system shall allow administrators to:

* View all orders.
* Update order status.

Supported order statuses:

* PENDING
* CONFIRMED
* SHIPPED
* DELIVERED
* CANCELLED

---

## Notifications

The system shall publish order events to Kafka.

The notification service shall consume events and send order confirmation notifications.

---

# Non-Functional Requirements

## Security

* JWT Authentication
* Password Encryption using BCrypt
* Role-Based Access Control

## Performance

* Product listing should support pagination.
* APIs should return responses within acceptable latency.

## Scalability

* Services should be independently deployable.
* Each service should own its database.

## Availability

* Service failures should not impact unrelated services.

## Containerization

The application shall support startup using Docker Compose.

Example:

docker compose up

---

# Technology Stack

Backend:

* Java 21
* Spring Boot
* Spring Cloud
* Spring Security

Frontend:

* React
* TypeScript

Database:

* MySQL

Messaging:

* Kafka

Deployment:

* Docker
* AWS

---

# Version 1.0 Scope

Included:

* Authentication
* Product Management
* Category Management
* Cart
* Orders
* Kafka Notifications
* Admin Dashboard

Excluded:

* Payments
* Wishlist
* Product Reviews
* Coupons
* Recommendation Engine
* Multi-Tenant Stores

These features will be delivered in future releases.
