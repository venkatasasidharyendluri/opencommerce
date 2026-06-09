package com.opencommerce.catalogservice.controller;

import com.opencommerce.catalogservice.dto.request.CreateCategoryRequest;
import com.opencommerce.catalogservice.dto.request.UpdateCategoryRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.CategoryResponse;
import com.opencommerce.catalogservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable UUID uuid) {

        return ResponseEntity.ok(categoryService.getCategory(uuid));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable UUID uuid,
            @Valid @RequestBody UpdateCategoryRequest request) {

        return ResponseEntity.ok(categoryService.updateCategory(uuid, request)
        );
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable UUID uuid) {

        return ResponseEntity.ok(categoryService.deleteCategory(uuid));
    }
}