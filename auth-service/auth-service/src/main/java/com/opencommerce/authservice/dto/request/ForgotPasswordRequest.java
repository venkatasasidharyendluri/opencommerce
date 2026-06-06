package com.opencommerce.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message ="Email Required")
        @Email(message = "Invalid Email")
        String email
) {
}
