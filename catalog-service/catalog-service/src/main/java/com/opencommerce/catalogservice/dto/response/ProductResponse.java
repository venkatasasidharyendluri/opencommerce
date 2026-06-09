package com.opencommerce.catalogservice.dto.response;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductResponse(

        UUID uuid,

        String name,

        String description,

        BigDecimal price,

        Integer stock,

        String brand,

        String sku,

        Boolean active,

        CategoryResponse category,

        Set<ProductImageResponse> images

) {
}


