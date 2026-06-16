package com.opencommerce.orderservice.dto.response;

import com.opencommerce.orderservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record OrderResponse(

        UUID uuid,

        String orderNumber,

        OrderStatus status,

        BigDecimal totalAmount,

        LocalDate estimatedDeliveryDate,

        String fullName,

        String mobileNumber,

        String addressLine1,

        String addressLine2,

        String city,

        String state,

        String country,

        String postalCode,

        List<OrderItemResponse> items

) {
}