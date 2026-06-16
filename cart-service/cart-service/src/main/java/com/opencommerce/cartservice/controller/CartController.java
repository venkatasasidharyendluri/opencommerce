package com.opencommerce.cartservice.controller;

import com.opencommerce.cartservice.dto.request.AddCartItemRequest;
import com.opencommerce.cartservice.dto.request.UpdateCartItemRequest;
import com.opencommerce.cartservice.dto.response.ApiResponse;
import com.opencommerce.cartservice.dto.response.CartResponse;
import com.opencommerce.cartservice.security.JwtService;
import com.opencommerce.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

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
    public ResponseEntity<ApiResponse> addItem(
            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            AddCartItemRequest request
    ) {

        return ResponseEntity.ok(
                cartService.addItem(
                        getUserUuid(
                                authHeader
                        ),
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        return ResponseEntity.ok(
                cartService.getCart(
                        getUserUuid(
                                authHeader
                        )
                )
        );
    }

    @PutMapping("/{itemUuid}")
    public ResponseEntity<ApiResponse> updateItem(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID itemUuid,

            @Valid
            @RequestBody
            UpdateCartItemRequest request
    ) {

        return ResponseEntity.ok(
                cartService.updateItem(
                        getUserUuid(
                                authHeader
                        ),
                        itemUuid,
                        request
                )
        );
    }

    @DeleteMapping("/{itemUuid}")
    public ResponseEntity<ApiResponse> removeItem(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID itemUuid
    ) {

        return ResponseEntity.ok(
                cartService.removeItem(
                        getUserUuid(
                                authHeader
                        ),
                        itemUuid
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> clearCart(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        return ResponseEntity.ok(
                cartService.clearCart(
                        getUserUuid(
                                authHeader
                        )
                )
        );
    }
}
