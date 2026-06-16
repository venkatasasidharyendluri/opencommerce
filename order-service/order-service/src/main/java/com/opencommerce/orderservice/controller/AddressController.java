package com.opencommerce.orderservice.controller;

import com.opencommerce.orderservice.dto.request.AddressRequest;
import com.opencommerce.orderservice.dto.response.AddressResponse;
import com.opencommerce.orderservice.security.JwtService;
import com.opencommerce.orderservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    private final JwtService jwtService;

    private UUID getUserUuid(
            String authHeader
    ) {

        String jwt =
                authHeader.substring(7);

        return UUID.fromString(
                jwtService.extractUserUuid(
                        jwt
                )
        );
    }

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(
            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            AddressRequest request
    ) {

        return ResponseEntity.ok(
                addressService.addAddress(
                        getUserUuid(
                                authHeader
                        ),
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>>
    getAddresses(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        return ResponseEntity.ok(
                addressService.getAddresses(
                        getUserUuid(
                                authHeader
                        )
                )
        );
    }

    @PutMapping("/{addressUuid}")
    public ResponseEntity<AddressResponse>
    updateAddress(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID addressUuid,

            @Valid
            @RequestBody
            AddressRequest request
    ) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        getUserUuid(
                                authHeader
                        ),
                        addressUuid,
                        request
                )
        );
    }

    @DeleteMapping("/{addressUuid}")
    public ResponseEntity<Void>
    deleteAddress(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID addressUuid
    ) {

        addressService.deleteAddress(
                getUserUuid(
                        authHeader
                ),
                addressUuid
        );

        return ResponseEntity.noContent()
                .build();
    }
}