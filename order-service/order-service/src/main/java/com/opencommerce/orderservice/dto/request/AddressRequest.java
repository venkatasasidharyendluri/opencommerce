package com.opencommerce.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(

        @NotBlank
        String fullName,

        @NotBlank
        String mobileNumber,

        @NotBlank
        String addressLine1,

        String addressLine2,

        @NotBlank
        String city,

        @NotBlank
        String state,

        @NotBlank
        String country,

        @NotBlank
        String postalCode,

        Boolean isDefault

) {}