package com.opencommerce.orderservice.service;

import com.opencommerce.orderservice.dto.request.AddressRequest;
import com.opencommerce.orderservice.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponse addAddress(
            UUID userUuid,
            AddressRequest request
    );

    List<AddressResponse> getAddresses(
            UUID userUuid
    );

    AddressResponse updateAddress(
            UUID userUuid,
            UUID addressUuid,
            AddressRequest request
    );

    void deleteAddress(
            UUID userUuid,
            UUID addressUuid
    );
}