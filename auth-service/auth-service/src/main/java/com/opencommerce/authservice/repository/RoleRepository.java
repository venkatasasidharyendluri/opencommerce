package com.opencommerce.authservice.repository;

import com.opencommerce.authservice.entity.Role;
import com.opencommerce.authservice.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName (RoleType name);
}
