package com.opencommerce.catalogservice.mapper;

import com.opencommerce.catalogservice.dto.response.ProductImageResponse;
import com.opencommerce.catalogservice.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImageResponse toResponse(
            ProductImage image
    ) {

        return new ProductImageResponse(
                image.getUuid(),
                image.getImageUrl(),
                image.getIsPrimary()
        );
    }
}