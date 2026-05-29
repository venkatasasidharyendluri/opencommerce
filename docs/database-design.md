# OpenCommerce Database Design v1.0

## Database Strategy

Each microservice owns its own database.

Services:

* auth_db
* catalog_db
* cart_db
* order_db

No service directly accesses another service's tables.

Communication occurs through REST APIs and Kafka events.

---

# AUTH SERVICE

Database: auth_db

## users

| Column         | Type         | Constraints   |
| -------------- | ------------ | ------------- |
| id             | BIGINT       | PK            |
| uuid           | CHAR(36)     | UNIQUE        |
| first_name     | VARCHAR(100) | NOT NULL      |
| last_name      | VARCHAR(100) | NOT NULL      |
| email          | VARCHAR(255) | UNIQUE        |
| password_hash  | VARCHAR(255) | NOT NULL      |
| enabled        | BOOLEAN      | DEFAULT TRUE  |
| email_verified | BOOLEAN      | DEFAULT FALSE |
| created_at     | DATETIME     | NOT NULL      |
| updated_at     | DATETIME     | NOT NULL      |

---

## roles

| Column | Type        |
| ------ | ----------- |
| id     | BIGINT      |
| name   | VARCHAR(50) |

Values:

* ROLE_ADMIN
* ROLE_CUSTOMER

---

## user_roles

| Column  | Type   |
| ------- | ------ |
| user_id | BIGINT |
| role_id | BIGINT |

Allows future support for multiple roles.

---

## refresh_tokens

| Column     | Type         |
| ---------- | ------------ |
| id         | BIGINT       |
| user_id    | BIGINT       |
| token      | VARCHAR(500) |
| expires_at | DATETIME     |

---

# CATALOG SERVICE

Database: catalog_db

## categories

| Column             | Type         |
| ------------------ | ------------ |
| id                 | BIGINT       |
| parent_category_id | BIGINT NULL  |
| name               | VARCHAR(255) |
| slug               | VARCHAR(255) |
| description        | TEXT         |
| created_at         | DATETIME     |
| updated_at         | DATETIME     |

Supports:

Electronics
└── Mobiles
└── Laptops

Fashion
└── Men
└── Women

---

## products

| Column            | Type          |
| ----------------- | ------------- |
| id                | BIGINT        |
| uuid              | CHAR(36)      |
| category_id       | BIGINT        |
| sku               | VARCHAR(100)  |
| name              | VARCHAR(255)  |
| slug              | VARCHAR(255)  |
| short_description | VARCHAR(500)  |
| description       | TEXT          |
| price             | DECIMAL(12,2) |
| stock_quantity    | INT           |
| status            | VARCHAR(50)   |
| created_at        | DATETIME      |
| updated_at        | DATETIME      |

---

## product_images

| Column        | Type          |
| ------------- | ------------- |
| id            | BIGINT        |
| product_id    | BIGINT        |
| image_url     | VARCHAR(1000) |
| display_order | INT           |
| is_primary    | BOOLEAN       |

---

## product_attributes

| Column          | Type         |
| --------------- | ------------ |
| id              | BIGINT       |
| product_id      | BIGINT       |
| attribute_name  | VARCHAR(100) |
| attribute_value | VARCHAR(255) |

Examples:

Color = Black

RAM = 8GB

Storage = 256GB

---

# CART SERVICE

Database: cart_db

## carts

| Column     | Type        |
| ---------- | ----------- |
| id         | BIGINT      |
| user_id    | BIGINT      |
| status     | VARCHAR(50) |
| created_at | DATETIME    |
| updated_at | DATETIME    |

Status:

* ACTIVE
* CHECKED_OUT
* ABANDONED

---

## cart_items

| Column                | Type          |
| --------------------- | ------------- |
| id                    | BIGINT        |
| cart_id               | BIGINT        |
| product_id            | BIGINT        |
| product_name_snapshot | VARCHAR(255)  |
| unit_price_snapshot   | DECIMAL(12,2) |
| quantity              | INT           |
| created_at            | DATETIME      |

Price snapshots prevent cart changes when product prices change.

---

# ORDER SERVICE

Database: order_db

## orders

| Column       | Type          |
| ------------ | ------------- |
| id           | BIGINT        |
| uuid         | CHAR(36)      |
| user_id      | BIGINT        |
| order_number | VARCHAR(50)   |
| total_amount | DECIMAL(12,2) |
| status       | VARCHAR(50)   |
| created_at   | DATETIME      |
| updated_at   | DATETIME      |

Status:

* PENDING
* CONFIRMED
* PAID
* SHIPPED
* DELIVERED
* CANCELLED
* REFUNDED

---

## order_items

| Column       | Type          |
| ------------ | ------------- |
| id           | BIGINT        |
| order_id     | BIGINT        |
| product_id   | BIGINT        |
| sku          | VARCHAR(100)  |
| product_name | VARCHAR(255)  |
| quantity     | INT           |
| unit_price   | DECIMAL(12,2) |
| total_price  | DECIMAL(12,2) |

Important:

Order items store snapshots.

Even if product name changes later, old orders remain correct.

---

## addresses

| Column         | Type         |
| -------------- | ------------ |
| id             | BIGINT       |
| user_id        | BIGINT       |
| full_name      | VARCHAR(255) |
| phone          | VARCHAR(20)  |
| address_line_1 | VARCHAR(255) |
| address_line_2 | VARCHAR(255) |
| city           | VARCHAR(100) |
| state          | VARCHAR(100) |
| postal_code    | VARCHAR(20)  |
| country        | VARCHAR(100) |
| created_at     | DATETIME     |

Users can save multiple addresses.

---

## order_addresses

| Column         | Type         |
| -------------- | ------------ |
| id             | BIGINT       |
| order_id       | BIGINT       |
| full_name      | VARCHAR(255) |
| phone          | VARCHAR(20)  |
| address_line_1 | VARCHAR(255) |
| address_line_2 | VARCHAR(255) |
| city           | VARCHAR(100) |
| state          | VARCHAR(100) |
| postal_code    | VARCHAR(20)  |
| country        | VARCHAR(100) |

Address snapshot stored when order is placed.

---

# Required Indexes

users(email)

products(category_id)

products(name)

products(status)

products(sku)

orders(user_id)

orders(order_number)

orders(status)

cart_items(cart_id)

order_items(order_id)

---

# Future Tables (v2)

payments

payment_transactions

wishlists

wishlist_items

reviews

review_images

coupons

coupon_usages

inventory_movements

audit_logs

store_settings

vendors

vendor_products
