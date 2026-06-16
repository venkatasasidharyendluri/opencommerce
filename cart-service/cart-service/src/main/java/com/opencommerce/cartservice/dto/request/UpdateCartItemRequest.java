package com.opencommerce.cartservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(

        @NotNull(message = "Quantity Required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity

) {
}