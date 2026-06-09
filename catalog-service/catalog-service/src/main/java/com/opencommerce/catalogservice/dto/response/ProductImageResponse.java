package com.opencommerce.catalogservice.dto.response;

import java.util.UUID;

public record ProductImageResponse(

        UUID uuid,

        String imageUrl,

        Boolean isPrimary

) {
}
