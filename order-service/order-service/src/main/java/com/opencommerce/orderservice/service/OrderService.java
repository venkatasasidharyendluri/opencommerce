package com.opencommerce.orderservice.service;

import com.opencommerce.orderservice.client.dto.ApiResponse;
import com.opencommerce.orderservice.dto.request.PlaceOrderRequest;
import com.opencommerce.orderservice.dto.response.OrderResponse;
import com.opencommerce.orderservice.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse placeOrder(
            UUID userUuid,
            String authHeader,
            PlaceOrderRequest request
    );

    List<OrderResponse> getOrders(
            UUID userUuid
    );

    OrderResponse getOrder(
            UUID userUuid,
            UUID orderUuid
    );

    ApiResponse cancelOrder(
            UUID userUuid,
            UUID orderUuid
    );

    ApiResponse updateOrderStatus(
            UUID orderUuid,
            OrderStatus status
    );
}