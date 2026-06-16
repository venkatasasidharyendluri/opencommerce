package com.opencommerce.orderservice.dto.response;

import java.util.UUID;

public record AddressResponse(

        UUID uuid,

        String fullName,

        String mobileNumber,

        String addressLine1,

        String addressLine2,

        String city,

        String state,

        String country,

        String postalCode,

        Boolean isDefault

) {}