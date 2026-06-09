package com.opencommerce.catalogservice.service;

import com.opencommerce.catalogservice.dto.request.CreateProductImageRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductImageResponse;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    ApiResponse uploadImage(
            UUID productUuid,
            CreateProductImageRequest request
    );

    ApiResponse deleteImage(
            UUID imageUuid
    );

    List<ProductImageResponse>
    getProductImages(
            UUID productUuid
    );
}