package com.opencommerce.orderservice.dto.external;

import java.util.List;
import java.util.UUID;

public record CartSnapshot(

        UUID uuid,

        UUID userUuid,

        List<CartItemSnapshot> items

) {
}