package com.opencommerce.orderservice.service.impl;

import com.opencommerce.orderservice.dto.request.AddressRequest;
import com.opencommerce.orderservice.dto.response.AddressResponse;
import com.opencommerce.orderservice.entity.Address;
import com.opencommerce.orderservice.mapper.AddressMapper;
import com.opencommerce.orderservice.repository.AddressRepository;
import com.opencommerce.orderservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl
        implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Override
    public AddressResponse addAddress(
            UUID userUuid,
            AddressRequest request
    ) {

        Address address =
                addressMapper.toEntity(
                        request
                );

        address.setUserUuid(
                userUuid
        );

        return addressMapper.toResponse(
                addressRepository.save(
                        address
                )
        );
    }

    @Override
    public List<AddressResponse> getAddresses(
            UUID userUuid
    ) {

        return addressRepository
                .findByUserUuid(
                        userUuid
                )
                .stream()
                .map(
                        addressMapper::toResponse
                )
                .toList();
    }

    @Override
    public AddressResponse updateAddress(
            UUID userUuid,
            UUID addressUuid,
            AddressRequest request
    ) {

        Address address =
                addressRepository
                        .findByUuid(
                                addressUuid
                        )
                        .orElseThrow();

        if (!address.getUserUuid().equals(userUuid)) {
            throw new RuntimeException(
                    "Access Denied"
            );
        }

        address.setFullName(
                request.fullName()
        );

        address.setMobileNumber(
                request.mobileNumber()
        );

        address.setAddressLine1(
                request.addressLine1()
        );

        address.setAddressLine2(
                request.addressLine2()
        );

        address.setCity(
                request.city()
        );

        address.setState(
                request.state()
        );

        address.setCountry(
                request.country()
        );

        address.setPostalCode(
                request.postalCode()
        );

        address.setIsDefault(
                request.isDefault()
        );

        return addressMapper.toResponse(
                addressRepository.save(
                        address
                )
        );
    }

    @Override
    public void deleteAddress(
            UUID userUuid,
            UUID addressUuid
    ) {

        Address address =
                addressRepository
                        .findByUuid(
                                addressUuid
                        )
                        .orElseThrow();

        if (!address.getUserUuid().equals(userUuid)) {
            throw new RuntimeException(
                    "Access Denied"
            );
        }

        addressRepository.delete(
                address
        );
    }
}