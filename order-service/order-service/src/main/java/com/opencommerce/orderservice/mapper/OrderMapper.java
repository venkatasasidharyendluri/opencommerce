package com.opencommerce.orderservice.mapper;

import com.opencommerce.orderservice.dto.response.OrderItemResponse;
import com.opencommerce.orderservice.dto.response.OrderResponse;
import com.opencommerce.orderservice.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(
            Order order
    ) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getProductUuid(),
                                        item.getProductName(),
                                        item.getProductImage(),
                                        item.getProductPrice(),
                                        item.getQuantity(),
                                        item.getSubTotal()
                                )
                        )
                        .toList();

        return new OrderResponse(
                order.getUuid(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getEstimatedDeliveryDate(),
                order.getFullName(),
                order.getMobileNumber(),
                order.getAddressLine1(),
                order.getAddressLine2(),
                order.getCity(),
                order.getState(),
                order.getCountry(),
                order.getPostalCode(),
                items
        );
    }
}