package com.opencommerce.cartservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AddCartItemRequest(

        @NotNull(message = "Product UUID Required")
        UUID productUuid,

        @NotBlank(message = "Product Name Required")
        String productName,

        @NotBlank(message = "Product Image Required")
        String productImage,

        @NotNull(message = "Product Price Required")
        BigDecimal productPrice,

        @NotNull(message = "Quantity Required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity

) {
}