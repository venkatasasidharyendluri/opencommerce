package com.opencommerce.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

        @NotBlank(message = "Token Required")
        String token,

        @NotBlank(message = "Password Required")
        @Size(
                min = 8,
                message = "Password must be at least 8 characters"
        )
        String newPassword

) {
}