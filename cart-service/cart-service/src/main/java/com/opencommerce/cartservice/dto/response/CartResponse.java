package com.opencommerce.cartservice.dto.response;

import java.util.List;
import java.util.UUID;

public record CartResponse(

        UUID uuid,

        UUID userUuid,

        List<CartItemResponse> items

) {
}