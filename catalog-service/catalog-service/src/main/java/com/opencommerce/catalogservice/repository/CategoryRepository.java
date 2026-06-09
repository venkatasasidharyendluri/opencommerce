package com.opencommerce.catalogservice.repository;


import com.opencommerce.catalogservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
        Optional<Category> findByUuid(UUID uuid);
        Boolean existsByName(String name);
        List<Category> findByActiveTrue();
}

