package com.opencommerce.catalogservice.dto.response;

import java.util.UUID;

public record CategoryResponse(

        UUID uuid,

        String name,

        String description,

        Boolean active

) {
}
