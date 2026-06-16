package com.opencommerce.orderservice.repository;

import com.opencommerce.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByUuid(
            UUID uuid
    );

    List<Order> findByUserUuidOrderByCreatedAtDesc(
            UUID userUuid
    );

    Optional<Order> findByOrderNumber(
            String orderNumber
    );
}