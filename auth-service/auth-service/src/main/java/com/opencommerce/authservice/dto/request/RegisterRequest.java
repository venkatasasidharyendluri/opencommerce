package com.opencommerce.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank (message = "First Name is required")
        @Size(max = 50, message = "First name must less than 50 characters")
        String firstName,

        @NotBlank (message = "Last Name is required")
        @Size(max = 50, message = "Last name must less than 50 characters")
        String lastName,


        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {
}
