package com.opencommerce.orderservice.dto.request;

import com.opencommerce.orderservice.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(

        @NotNull
        OrderStatus status

) {
}