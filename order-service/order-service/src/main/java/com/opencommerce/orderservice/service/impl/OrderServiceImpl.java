package com.opencommerce.orderservice.service.impl;

import com.opencommerce.orderservice.client.CartClient;
import com.opencommerce.orderservice.client.dto.ApiResponse;
import com.opencommerce.orderservice.dto.external.CartItemSnapshot;
import com.opencommerce.orderservice.dto.external.CartSnapshot;
import com.opencommerce.orderservice.dto.request.PlaceOrderRequest;
import com.opencommerce.orderservice.dto.response.OrderResponse;
import com.opencommerce.orderservice.entity.Address;
import com.opencommerce.orderservice.entity.Order;
import com.opencommerce.orderservice.entity.OrderItem;
import com.opencommerce.orderservice.enums.OrderStatus;
import com.opencommerce.orderservice.mapper.OrderMapper;
import com.opencommerce.orderservice.repository.AddressRepository;
import com.opencommerce.orderservice.repository.OrderRepository;
import com.opencommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl
        implements OrderService {

    private final OrderRepository orderRepository;

    private final AddressRepository addressRepository;

    private final CartClient cartClient;

    private final OrderMapper orderMapper;

    @Override
    public OrderResponse placeOrder(
            UUID userUuid,
            String authHeader,
            PlaceOrderRequest request
    ) {

        Address address =
                addressRepository
                        .findByUuid(
                                request.addressUuid()
                        )
                        .orElseThrow();

        if (!address.getUserUuid().equals(userUuid)) {
            throw new RuntimeException(
                    "Access Denied"
            );
        }

        CartSnapshot cart =
                cartClient.getCart(
                        authHeader
                );

        BigDecimal totalAmount =
                cart.items()
                        .stream()
                        .map(
                                CartItemSnapshot::subTotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        Order order =
                Order.builder()
                        .userUuid(userUuid)
                        .orderNumber(
                                "ORD-" +
                                        System.currentTimeMillis()
                        )
                        .totalAmount(
                                totalAmount
                        )
                        .status(
                                OrderStatus.CONFIRMED
                        )
                        .estimatedDeliveryDate(
                                LocalDate.now()
                                        .plusDays(5)
                        )

                        .fullName(
                                address.getFullName()
                        )
                        .mobileNumber(
                                address.getMobileNumber()
                        )
                        .addressLine1(
                                address.getAddressLine1()
                        )
                        .addressLine2(
                                address.getAddressLine2()
                        )
                        .city(
                                address.getCity()
                        )
                        .state(
                                address.getState()
                        )
                        .country(
                                address.getCountry()
                        )
                        .postalCode(
                                address.getPostalCode()
                        )
                        .build();

        cart.items()
                .forEach(item -> {

                    OrderItem orderItem =
                            OrderItem.builder()
                                    .productUuid(
                                            item.productUuid()
                                    )
                                    .productName(
                                            item.productName()
                                    )
                                    .productImage(
                                            item.productImage()
                                    )
                                    .productPrice(
                                            item.productPrice()
                                    )
                                    .quantity(
                                            item.quantity()
                                    )
                                    .subTotal(
                                            item.subTotal()
                                    )
                                    .order(
                                            order
                                    )
                                    .build();

                    order.getItems()
                            .add(orderItem);
                });

        Order savedOrder =
                orderRepository.save(
                        order
                );

        cartClient.clearCart(
                authHeader
        );

        return orderMapper.toResponse(
                savedOrder
        );
    }

    @Override
    public List<OrderResponse> getOrders(
            UUID userUuid
    ) {

        return orderRepository
                .findByUserUuidOrderByCreatedAtDesc(
                        userUuid
                )
                .stream()
                .map(
                        orderMapper::toResponse
                )
                .toList();
    }

    @Override
    public OrderResponse getOrder(
            UUID userUuid,
            UUID orderUuid
    ) {

        Order order =
                orderRepository
                        .findByUuid(
                                orderUuid
                        )
                        .orElseThrow();

        if (!order.getUserUuid().equals(userUuid)) {
            throw new RuntimeException(
                    "Access Denied"
            );
        }

        return orderMapper.toResponse(
                order
        );
    }

    @Override
    public ApiResponse cancelOrder(
            UUID userUuid,
            UUID orderUuid
    ) {

        Order order =
                orderRepository
                        .findByUuid(
                                orderUuid
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order Not Found"
                                )
                        );

        if (
                !order.getUserUuid()
                        .equals(userUuid)
        ) {

            throw new RuntimeException(
                    "Access Denied"
            );
        }

        if (
                order.getStatus()
                        == OrderStatus.SHIPPED
                        ||
                        order.getStatus()
                                == OrderStatus.DELIVERED
        ) {

            throw new RuntimeException(
                    "Order Cannot Be Cancelled"
            );
        }

        if (
                order.getStatus()
                        == OrderStatus.CANCELLED
        ) {

            throw new RuntimeException(
                    "Order Already Cancelled"
            );
        }

        order.setStatus(
                OrderStatus.CANCELLED
        );

        orderRepository.save(
                order
        );

        return new ApiResponse(
                true,
                "Order Cancelled Successfully"
        );
    }

    @Override
    public ApiResponse updateOrderStatus(
            UUID orderUuid,
            OrderStatus status
    ) {

        Order order =
                orderRepository
                        .findByUuid(orderUuid)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order Not Found"
                                )
                        );

        if (
                order.getStatus()
                        == OrderStatus.CANCELLED
        ) {

            throw new RuntimeException(
                    "Cancelled Order Cannot Be Updated"
            );
        }

        order.setStatus(
                status
        );

        orderRepository.save(
                order
        );

        return new ApiResponse(
                true,
                "Order Status Updated Successfully"
        );
    }
}