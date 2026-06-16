package com.opencommerce.cartservice.repository;

import com.opencommerce.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUuid(UUID uuid);

    Optional<Cart> findByUserUuidAndActiveTrue(UUID userUuid);
}