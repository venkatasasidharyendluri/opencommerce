package com.opencommerce.orderservice.controller;

import com.opencommerce.orderservice.client.dto.ApiResponse;
import com.opencommerce.orderservice.dto.request.PlaceOrderRequest;
import com.opencommerce.orderservice.dto.request.UpdateOrderStatusRequest;
import com.opencommerce.orderservice.dto.response.OrderResponse;
import com.opencommerce.orderservice.security.JwtService;
import com.opencommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final JwtService jwtService;

    private UUID getUserUuid(
            String authHeader
    ) {

        String jwt =
                authHeader.substring(7);

        return UUID.fromString(
                jwtService.extractUserUuid(jwt)
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("Authorization")
            String authHeader,

            @Valid
            @RequestBody
            PlaceOrderRequest request
    ) {

        return ResponseEntity.ok(
                orderService.placeOrder(
                        getUserUuid(authHeader),
                        authHeader,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        return ResponseEntity.ok(
                orderService.getOrders(
                        getUserUuid(authHeader)
                )
        );
    }

    @GetMapping("/{orderUuid}")
    public ResponseEntity<OrderResponse> getOrder(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID orderUuid
    ) {

        return ResponseEntity.ok(
                orderService.getOrder(
                        getUserUuid(authHeader),
                        orderUuid
                )
        );
    }

    @PatchMapping("/{orderUuid}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(
            @RequestHeader("Authorization")
            String authHeader,

            @PathVariable
            UUID orderUuid
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(
                        getUserUuid(authHeader),
                        orderUuid
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderUuid}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(

            @PathVariable
            UUID orderUuid,

            @Valid
            @RequestBody
            UpdateOrderStatusRequest request
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderUuid,
                        request.status()
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>>
    getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{orderUuid}")
    public ResponseEntity<OrderResponse> getOrderForAdmin(
            @PathVariable UUID orderUuid
    ) {
        return ResponseEntity.ok(
                orderService.getOrderForAdmin(orderUuid)
        );
    }
}