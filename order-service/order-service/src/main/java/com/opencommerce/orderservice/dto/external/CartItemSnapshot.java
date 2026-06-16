package com.opencommerce.orderservice.dto.external;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemSnapshot(

        UUID uuid,

        UUID productUuid,

        String productName,

        String productImage,

        BigDecimal productPrice,

        Integer quantity,

        BigDecimal subTotal

) {
}