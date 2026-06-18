package com.opencommerce.catalogservice.repository;

import com.opencommerce.catalogservice.entity.Category;
import com.opencommerce.catalogservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByUuid(UUID uuid);

    Boolean existsBySku(String sku);

    List<Product> findByActiveTrue();

    List<Product> findByActiveTrueAndCategoryActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);

    Optional<Product> findByUuidAndActiveTrue(UUID uuid);
}
