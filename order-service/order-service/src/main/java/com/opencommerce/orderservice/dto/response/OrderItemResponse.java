package com.opencommerce.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(

        UUID productUuid,

        String productName,

        String productImage,

        BigDecimal productPrice,

        Integer quantity,

        BigDecimal subTotal

) {
}