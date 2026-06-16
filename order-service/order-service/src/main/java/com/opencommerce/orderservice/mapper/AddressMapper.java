package com.opencommerce.orderservice.mapper;

import com.opencommerce.orderservice.dto.request.AddressRequest;
import com.opencommerce.orderservice.dto.response.AddressResponse;
import com.opencommerce.orderservice.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(
            AddressRequest request
    ) {

        return Address.builder()
                .fullName(request.fullName())
                .mobileNumber(request.mobileNumber())
                .addressLine1(request.addressLine1())
                .addressLine2(request.addressLine2())
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .postalCode(request.postalCode())
                .isDefault(
                        request.isDefault() != null
                                ? request.isDefault()
                                : false
                )
                .build();
    }

    public AddressResponse toResponse(
            Address address
    ) {

        return new AddressResponse(
                address.getUuid(),
                address.getFullName(),
                address.getMobileNumber(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                address.getIsDefault()
        );
    }
}