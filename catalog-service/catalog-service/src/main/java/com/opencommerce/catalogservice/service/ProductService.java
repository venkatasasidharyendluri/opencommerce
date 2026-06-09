package com.opencommerce.catalogservice.service;

import com.opencommerce.catalogservice.dto.request.CreateProductRequest;
import com.opencommerce.catalogservice.dto.request.UpdateProductRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ApiResponse createProduct(CreateProductRequest request);
    ProductResponse getProduct(UUID uuid );
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(UUID categoryUuid);
    ApiResponse updateProduct(UUID uuid, UpdateProductRequest request);
    ApiResponse deleteProduct(UUID uuid);
}
