package com.opencommerce.cartservice.service;

import com.opencommerce.cartservice.dto.request.AddCartItemRequest;
import com.opencommerce.cartservice.dto.request.UpdateCartItemRequest;
import com.opencommerce.cartservice.dto.response.ApiResponse;
import com.opencommerce.cartservice.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {

    ApiResponse addItem(UUID userUuid, AddCartItemRequest request);

    CartResponse getCart(UUID userUuid);

    ApiResponse updateItem(
            UUID userUuid,
            UUID itemUuid,
            UpdateCartItemRequest request
    );

    ApiResponse removeItem(
            UUID userUuid,
            UUID itemUuid
    );

    ApiResponse clearCart(
            UUID userUuid
    );
}