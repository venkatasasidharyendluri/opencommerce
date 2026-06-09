package com.opencommerce.catalogservice.dto.request;

import java.math.BigDecimal;

public record UpdateProductRequest(

        String name,

        String description,

        BigDecimal price,

        Integer stock,

        String brand,

        Boolean active

) {
}
