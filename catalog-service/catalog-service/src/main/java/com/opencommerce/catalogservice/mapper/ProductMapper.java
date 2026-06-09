package com.opencommerce.catalogservice.mapper;

import com.opencommerce.catalogservice.dto.response.ProductImageResponse;
import com.opencommerce.catalogservice.dto.response.ProductResponse;
import com.opencommerce.catalogservice.entity.Product;
import com.opencommerce.catalogservice.entity.ProductImage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public ProductImageResponse toImageResponse(ProductImage image){
        return new ProductImageResponse(
                image.getUuid(),
                image.getImageUrl(),
                image.getIsPrimary()
        );
    }

    public ProductResponse toResponse(Product product){

        Set<ProductImageResponse> images =
                product.getImages()
                        .stream()
                        .map(this::toImageResponse)
                        .collect(Collectors.toSet());

        return new ProductResponse(
                product.getUuid(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getBrand(),
                product.getSku(),
                product.getActive(),
                categoryMapper.toResponse(product.getCategory()),
                images
        );
    }
}
