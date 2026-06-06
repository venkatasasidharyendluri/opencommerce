package com.opencommerce.authservice.repository;

import com.opencommerce.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(UUID uuid);
    Boolean existsByEmail(String email);
}
