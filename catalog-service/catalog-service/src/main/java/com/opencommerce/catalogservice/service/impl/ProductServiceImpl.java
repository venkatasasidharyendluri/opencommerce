package com.opencommerce.catalogservice.service.impl;

import com.opencommerce.catalogservice.dto.request.CreateProductRequest;
import com.opencommerce.catalogservice.dto.request.UpdateProductRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductResponse;
import com.opencommerce.catalogservice.entity.Category;
import com.opencommerce.catalogservice.entity.Product;
import com.opencommerce.catalogservice.exception.CategoryNotFoundException;
import com.opencommerce.catalogservice.exception.ProductAlreadyExistsException;
import com.opencommerce.catalogservice.exception.ProductNotFoundException;
import com.opencommerce.catalogservice.mapper.ProductMapper;
import com.opencommerce.catalogservice.repository.CategoryRepository;
import com.opencommerce.catalogservice.repository.ProductRepository;
import com.opencommerce.catalogservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ApiResponse createProduct(CreateProductRequest request) {

        if(productRepository.existsBySku(request.sku())) {
            throw new ProductAlreadyExistsException("Product Already Exists");
        }
        Category category =
                categoryRepository
                        .findByUuid(
                                request.categoryUuid()
                        )
                        .orElseThrow(
                                () ->
                                        new CategoryNotFoundException(
                                                "Category Not Found"
                                        )
                        );

        Product product =
                Product.builder()
                        .name(request.name())
                        .description(request.description())
                        .price(request.price())
                        .stock(request.stock())
                        .brand(request.brand())
                        .sku(request.sku())
                        .category(category)
                        .build();

        productRepository.save(product);


        return new ApiResponse(true,"Product created");
    }

    @Override
    public ProductResponse getProduct(UUID uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product Not Found")
                );
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(UUID categoryUuid) {
        Category category =
                categoryRepository
                        .findByUuid(categoryUuid)
                        .orElseThrow(() -> new CategoryNotFoundException("Category Not Found"));

        return productRepository
                .findByCategoryAndActiveTrue(category)
                .stream()
                .map(productMapper::toResponse)
                .toList();

    }

    @Override
    public ApiResponse updateProduct(UUID uuid, UpdateProductRequest request) {

        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product Not Found")
                );
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setBrand(request.brand());
        product.setActive(request.active());

        productRepository.save(product);
        return new ApiResponse(true, "Product Updated Successfully");
    }

    @Override
    public ApiResponse deleteProduct(UUID uuid) {
        Product product = productRepository.findByUuidAndActiveTrue(uuid)
                .orElseThrow(
                        () -> new ProductNotFoundException("Product Not Found")
                );
        product.setActive(false);
        productRepository.save(product);
        return new ApiResponse(true, "Product Deleted Successfully");
    }
}
