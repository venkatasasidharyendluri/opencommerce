package com.opencommerce.catalogservice.controller;

import com.opencommerce.catalogservice.dto.request.CreateProductImageRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.ProductImageResponse;
import com.opencommerce.catalogservice.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService
            productImageService;

    @PostMapping("/{productUuid}/images")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    uploadImage(
            @PathVariable UUID productUuid,
            @Valid
            @RequestBody
            CreateProductImageRequest request
    ) {

        return ResponseEntity.ok(
                productImageService
                        .uploadImage(
                                productUuid,
                                request
                        )
        );
    }

    @GetMapping("/{productUuid}/images")
    public ResponseEntity<
            List<ProductImageResponse>>
    getImages(
            @PathVariable UUID productUuid
    ) {

        return ResponseEntity.ok(
                productImageService
                        .getProductImages(
                                productUuid
                        )
        );
    }

    @DeleteMapping("/images/{imageUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse>
    deleteImage(
            @PathVariable UUID imageUuid
    ) {

        return ResponseEntity.ok(
                productImageService
                        .deleteImage(
                                imageUuid
                        )
        );
    }
}