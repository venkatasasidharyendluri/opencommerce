package com.opencommerce.catalogservice.controller;

import com.opencommerce.catalogservice.dto.request.CreateProductRequest;
import com.opencommerce.catalogservice.dto.request.UpdateProductRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductResponse;
import com.opencommerce.catalogservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@Valid
                                                         @RequestBody CreateProductRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProductsByActive() {

        return ResponseEntity.ok(productService.getAllProductsByActive());

    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID uuid) {

        return ResponseEntity.ok(productService.getProduct(uuid));

    }

    @GetMapping("/category/{categoryUuid}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable UUID categoryUuid) {

        return ResponseEntity.ok(productService.getProductsByCategory(categoryUuid));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable UUID uuid,
                                                     @Valid @RequestBody UpdateProductRequest request) {

        return ResponseEntity.ok(productService.updateProduct(uuid, request));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable UUID uuid) {

        return ResponseEntity.ok(productService.deleteProduct(uuid));
    }
}