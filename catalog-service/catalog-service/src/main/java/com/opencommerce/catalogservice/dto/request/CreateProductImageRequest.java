package com.opencommerce.catalogservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateProductImageRequest(

        @NotBlank(message = "Image URL Required")
        String imageUrl,

        Boolean isPrimary

) {
}