package com.opencommerce.catalogservice.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(

        @NotBlank(message = "Product name required")
        @Size(max = 50)
        String name,

        @NotBlank(message = "Description required")
        @Size(max = 1000)
        String description,

        @NotNull(message = "Price required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        @NotNull(message = "Stock required")
        @PositiveOrZero(message = "Stock cannot be negative")
        Integer stock,

        @NotBlank(message = "Brand required")
        String brand,

        @NotBlank(message = "SKU required")
        String sku,

        @NotNull(message = "Category required")
        UUID categoryUuid

) {
}
