package com.opencommerce.cartservice.repository;

import com.opencommerce.cartservice.entity.Cart;
import com.opencommerce.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUuid(UUID uuid);

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProductUuid(Cart cart, UUID productUuid);
}