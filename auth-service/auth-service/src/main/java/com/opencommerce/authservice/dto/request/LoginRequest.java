package com.opencommerce.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email Required")
        @Email
        String email,

        @NotBlank (message = "Password Required")
        String password
) {
}

