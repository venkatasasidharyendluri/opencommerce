package com.opencommerce.cartservice.service.impl;

import com.opencommerce.cartservice.dto.request.AddCartItemRequest;
import com.opencommerce.cartservice.dto.request.UpdateCartItemRequest;
import com.opencommerce.cartservice.dto.response.ApiResponse;
import com.opencommerce.cartservice.dto.response.CartResponse;
import com.opencommerce.cartservice.entity.Cart;
import com.opencommerce.cartservice.entity.CartItem;
import com.opencommerce.cartservice.exception.AccessDeniedException;
import com.opencommerce.cartservice.exception.CartItemNotFoundException;
import com.opencommerce.cartservice.exception.CartNotFoundException;
import com.opencommerce.cartservice.mapper.CartMapper;
import com.opencommerce.cartservice.repository.CartItemRepository;
import com.opencommerce.cartservice.repository.CartRepository;
import com.opencommerce.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl
        implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final CartMapper cartMapper;

    @Override
    public ApiResponse addItem(
            UUID userUuid,
            AddCartItemRequest request
    ) {

        Cart cart =
                cartRepository
                        .findByUserUuidAndActiveTrue(
                                userUuid
                        )
                        .orElseGet(() -> {

                            Cart newCart =
                                    Cart.builder()
                                            .userUuid(
                                                    userUuid
                                            )
                                            .build();

                            return cartRepository.save(
                                    newCart
                            );
                        });

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProductUuid(
                                cart,
                                request.productUuid()
                        )
                        .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity()
                            + request.quantity()
            );

            cartItem.setProductName(
                    request.productName()
            );

            cartItem.setProductImage(
                    request.productImage()
            );

            cartItem.setProductPrice(
                    request.productPrice()
            );

            cartItemRepository.save(
                    cartItem
            );
        } else {

            CartItem newItem =
                    CartItem.builder()
                            .productUuid(
                                    request.productUuid()
                            )
                            .productName(
                                    request.productName()
                            )
                            .productImage(
                                    request.productImage()
                            )
                            .productPrice(
                                    request.productPrice()
                            )
                            .quantity(
                                    request.quantity()
                            )
                            .cart(
                                    cart
                            )
                            .build();

            cartItemRepository.save(
                    newItem
            );
        }

        return new ApiResponse(
                true,
                "Item Added To Cart"
        );
    }

    @Override
    public CartResponse getCart(
            UUID userUuid
    ) {

        Cart cart =
                cartRepository
                        .findByUserUuidAndActiveTrue(
                                userUuid
                        )
                        .orElseThrow(
                                () -> new CartNotFoundException("Cart Not Found")
                        );

        return cartMapper.toResponse(
                cart
        );
    }

    @Override
    public ApiResponse updateItem(
            UUID userUuid,
            UUID itemUuid,
            UpdateCartItemRequest request
    ) {

        CartItem item =
                cartItemRepository
                        .findByUuid(itemUuid)
                        .orElseThrow(
                                () ->
                                        new CartItemNotFoundException(
                                                "Cart Item Not Found"
                                        )
                        );

        if (!item.getCart()
                .getUserUuid()
                .equals(userUuid)) {

            throw new AccessDeniedException(
                    "Access Denied"
            );
        }

        item.setQuantity(
                request.quantity()
        );

        cartItemRepository.save(item);

        return new ApiResponse(
                true,
                "Cart Updated Successfully"
        );
    }

    @Override
    public ApiResponse removeItem(
            UUID userUuid,
            UUID itemUuid
    ) {

        CartItem item =
                cartItemRepository
                        .findByUuid(itemUuid)
                        .orElseThrow(
                                () ->
                                        new CartItemNotFoundException(
                                                "Cart Item Not Found"
                                        )
                        );

        if (!item.getCart()
                .getUserUuid()
                .equals(userUuid)) {

            throw new AccessDeniedException(
                    "Access Denied"
            );
        }

        cartItemRepository.delete(item);

        return new ApiResponse(
                true,
                "Item Removed Successfully"
        );
    }

    @Override
    public ApiResponse clearCart(
            UUID userUuid
    ) {

        Cart cart =
                cartRepository
                        .findByUserUuidAndActiveTrue(
                                userUuid
                        )
                        .orElseThrow(
                                () -> new CartNotFoundException("Cart Not Found")
                        );

        cart.getItems().clear();

        cartRepository.save(
                cart
        );

        return new ApiResponse(
                true,
                "Cart Cleared Successfully"
        );
    }
}