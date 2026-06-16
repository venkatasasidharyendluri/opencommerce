package com.opencommerce.orderservice.repository;

import com.opencommerce.orderservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserUuid(UUID userUuid);

    Optional<Address> findByUuid(UUID uuid);
}