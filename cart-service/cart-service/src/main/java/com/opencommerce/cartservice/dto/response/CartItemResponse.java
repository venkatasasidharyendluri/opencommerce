package com.opencommerce.cartservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(

        UUID uuid,

        UUID productUuid,

        String productName,

        String productImage,

        BigDecimal productPrice,

        Integer quantity,

        BigDecimal subTotal

) {
}