package com.opencommerce.catalogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(

        @NotBlank(message = "Category name required")
        @Size(max = 50)
        String name,

        @NotBlank(message = "Description required")
        @Size(max = 1000)
        String description,

        Boolean active

) {
}
