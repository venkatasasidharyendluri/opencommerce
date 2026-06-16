package com.opencommerce.orderservice.repository;

import com.opencommerce.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}