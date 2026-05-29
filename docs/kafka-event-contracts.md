# OpenCommerce Kafka Event Contracts v1.0

## Overview

OpenCommerce uses Apache Kafka for asynchronous communication between microservices.

Goals:

* Loose coupling between services
* Event-driven architecture
* Independent service deployment
* Improved scalability

---

# Kafka Topics

| Topic                | Producer        | Consumer             |
| -------------------- | --------------- | -------------------- |
| order-created        | Order Service   | Notification Service |
| order-status-updated | Order Service   | Notification Service |
| product-created      | Catalog Service | Future Services      |
| product-updated      | Catalog Service | Future Services      |
| inventory-updated    | Catalog Service | Future Services      |

---

# Event Standards

Every event must contain:

```json
{
  "eventId": "uuid",
  "eventType": "ORDER_CREATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "order-service",
  "payload": {}
}
```

Rules:

* eventId must be UUID.
* eventVersion starts with 1.0.
* timestamp must be UTC.
* payload contains business data.

---

# ORDER CREATED EVENT

Topic:

order-created

Producer:

Order Service

Consumers:

* Notification Service

Purpose:

Published after successful order placement.

---

## Event Payload

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "eventType": "ORDER_CREATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "order-service",
  "payload": {
    "orderId": 1001,
    "orderNumber": "ORD-20260529-1001",
    "userId": 10,
    "customerEmail": "user@gmail.com",
    "totalAmount": 99999.00,
    "status": "PENDING"
  }
}
```

---

# ORDER STATUS UPDATED EVENT

Topic:

order-status-updated

Producer:

Order Service

Consumers:

* Notification Service

Purpose:

Published whenever order status changes.

---

## Event Payload

```json
{
  "eventId": "uuid",
  "eventType": "ORDER_STATUS_UPDATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "order-service",
  "payload": {
    "orderId": 1001,
    "orderNumber": "ORD-20260529-1001",
    "oldStatus": "CONFIRMED",
    "newStatus": "SHIPPED",
    "customerEmail": "user@gmail.com"
  }
}
```

---

# PRODUCT CREATED EVENT

Topic:

product-created

Producer:

Catalog Service

Consumers:

Future:

* Analytics Service
* Recommendation Service
* Search Service

Purpose:

Published whenever a product is created.

---

## Event Payload

```json
{
  "eventId": "uuid",
  "eventType": "PRODUCT_CREATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "catalog-service",
  "payload": {
    "productId": 1,
    "sku": "MAC-AIR-M3",
    "name": "MacBook Air",
    "categoryId": 1,
    "price": 99999
  }
}
```

---

# PRODUCT UPDATED EVENT

Topic:

product-updated

Producer:

Catalog Service

Consumers:

Future:

* Analytics Service
* Search Service

Purpose:

Published whenever product details change.

---

## Event Payload

```json
{
  "eventId": "uuid",
  "eventType": "PRODUCT_UPDATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "catalog-service",
  "payload": {
    "productId": 1,
    "name": "MacBook Air",
    "price": 109999,
    "stockQuantity": 20
  }
}
```

---

# INVENTORY UPDATED EVENT

Topic:

inventory-updated

Producer:

Catalog Service

Consumers:

Future:

* Analytics Service

Purpose:

Published when stock changes.

---

## Event Payload

```json
{
  "eventId": "uuid",
  "eventType": "INVENTORY_UPDATED",
  "eventVersion": "1.0",
  "timestamp": "2026-05-29T10:00:00Z",
  "source": "catalog-service",
  "payload": {
    "productId": 1,
    "sku": "MAC-AIR-M3",
    "oldQuantity": 50,
    "newQuantity": 45
  }
}
```

---

# Dead Letter Queue Strategy

Every consumer must handle failures.

Dead Letter Topics:

```text
order-created-dlt

order-status-updated-dlt

product-created-dlt

product-updated-dlt

inventory-updated-dlt
```

Failed events are redirected after retry exhaustion.

---

# Retry Strategy

Configuration:

* Retry Count: 3
* Backoff Delay: 5 seconds

Example:

Attempt 1

↓

Attempt 2

↓

Attempt 3

↓

Dead Letter Topic

---

# Message Serialization

Format:

JSON

Spring Kafka Serializer:

```java
JsonSerializer
```

Spring Kafka Deserializer:

```java
JsonDeserializer
```

---

# Topic Naming Convention

Format:

service-action

Examples:

order-created

order-status-updated

product-created

inventory-updated

---

# Future Events (v2)

payment-created

payment-completed

payment-failed

review-created

wishlist-created

coupon-applied

vendor-created

store-created

recommendation-generated

---

# Event Versioning Strategy

Current:

1.0

Future:

1.1

2.0

Breaking changes require new versions.

Example:

ORDER_CREATED_V2

Separate consumers can process different versions safely.
