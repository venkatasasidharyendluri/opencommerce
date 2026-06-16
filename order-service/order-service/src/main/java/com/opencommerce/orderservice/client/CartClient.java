package com.opencommerce.orderservice.client;

import com.opencommerce.orderservice.client.dto.ApiResponse;
import com.opencommerce.orderservice.dto.external.CartSnapshot;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient( name = "cart-service" )
public interface CartClient {

    @GetMapping("/api/v1/cart")
    CartSnapshot getCart(
            @RequestHeader("Authorization")
            String authorization
    );

    @DeleteMapping("/api/v1/cart")
    ApiResponse clearCart(@RequestHeader("Authorization") String authorization);
}