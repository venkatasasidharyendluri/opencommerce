package com.opencommerce.catalogservice.repository;

import com.opencommerce.catalogservice.entity.Product;
import com.opencommerce.catalogservice.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByUuid(
            UUID uuid
    );

    List<ProductImage> findByProduct(
            Product product
    );
}