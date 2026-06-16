package com.opencommerce.catalogservice.service.impl;

import com.opencommerce.catalogservice.dto.request.CreateProductImageRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductImageResponse;
import com.opencommerce.catalogservice.entity.Product;
import com.opencommerce.catalogservice.entity.ProductImage;
import com.opencommerce.catalogservice.exception.ProductImageNotFoundException;
import com.opencommerce.catalogservice.exception.ProductNotFoundException;
import com.opencommerce.catalogservice.mapper.ProductImageMapper;
import com.opencommerce.catalogservice.repository.ProductImageRepository;
import com.opencommerce.catalogservice.repository.ProductRepository;
import com.opencommerce.catalogservice.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl
        implements ProductImageService {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    @Override
    public ApiResponse uploadImage(
            UUID productUuid,
            CreateProductImageRequest request
    ) {

        Product product =
                productRepository
                        .findByUuid(productUuid)
                        .orElseThrow(
                                () -> new ProductNotFoundException(
                                        "Product Not Found"
                                )
                        );

        Boolean primary =
                Boolean.TRUE.equals(
                        request.isPrimary()
                );

        if (primary) {

            product.getImages()
                    .forEach(existingImage ->
                            existingImage.setIsPrimary(false)
                    );
        }

        ProductImage image =
                ProductImage.builder()
                        .imageUrl(
                                request.imageUrl()
                        )
                        .isPrimary(primary)
                        .product(product)
                        .build();

        productImageRepository.save(image);

        return new ApiResponse(
                true,
                "Image Uploaded Successfully"
        );
    }

    @Override
    public ApiResponse deleteImage(
            UUID imageUuid
    ) {

        ProductImage image =
                productImageRepository
                        .findByUuid(imageUuid)
                        .orElseThrow(
                                () ->
                                        new ProductImageNotFoundException(
                                                "Image Not Found"
                                        )
                        );

        productImageRepository.delete(image);

        return new ApiResponse(
                true,
                "Image Deleted Successfully"
        );
    }

    @Override
    public List<ProductImageResponse>
    getProductImages(
            UUID productUuid
    ) {

        Product product =
                productRepository
                        .findByUuid(productUuid)
                        .orElseThrow(
                                () ->
                                        new ProductNotFoundException(
                                                "Product Not Found"
                                        )
                        );

        return productImageRepository
                .findByProduct(product)
                .stream()
                .map(productImageMapper::toResponse)
                .toList();
    }
}