package com.opencommerce.catalogservice.service;

import com.opencommerce.catalogservice.dto.request.CreateCategoryRequest;
import com.opencommerce.catalogservice.dto.request.UpdateCategoryRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    ApiResponse createCategory(CreateCategoryRequest request);

    CategoryResponse getCategory(UUID uuid);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getAllCategoriesOnlyActive();

    ApiResponse updateCategory(UUID uuid, UpdateCategoryRequest request);

    ApiResponse deleteCategory(UUID uuid);
}