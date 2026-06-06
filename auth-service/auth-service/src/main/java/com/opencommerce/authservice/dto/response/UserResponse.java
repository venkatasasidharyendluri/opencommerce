package com.opencommerce.authservice.dto.response;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        Long id,
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        Set<String> roles
) {
}
