package com.opencommerce.cartservice.mapper;

import com.opencommerce.cartservice.dto.response.CartItemResponse;
import com.opencommerce.cartservice.dto.response.CartResponse;
import com.opencommerce.cartservice.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CartItemMapper cartItemMapper;

    public CartResponse toResponse(Cart cart) {

        List<CartItemResponse> items =
                cart.getItems()
                        .stream()
                        .map(cartItemMapper::toResponse)
                        .toList();

        return new CartResponse(cart.getUuid(),
                cart.getUserUuid(),
                items
        );
    }
}