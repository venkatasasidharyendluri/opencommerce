package com.opencommerce.orderservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlaceOrderRequest(

        @NotNull
        UUID addressUuid

) {
}