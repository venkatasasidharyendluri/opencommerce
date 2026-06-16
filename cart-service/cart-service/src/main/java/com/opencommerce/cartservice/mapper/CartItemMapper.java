package com.opencommerce.cartservice.mapper;

import com.opencommerce.cartservice.dto.response.CartItemResponse;
import com.opencommerce.cartservice.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {

    public CartItemResponse toResponse(
            CartItem cartItem
    ) {

        return new CartItemResponse(
                cartItem.getUuid(),
                cartItem.getProductUuid(),
                cartItem.getProductName(),
                cartItem.getProductImage(),
                cartItem.getProductPrice(),
                cartItem.getQuantity(),
                cartItem.getProductPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        cartItem.getQuantity()
                                )
                        )
        );
    }
}