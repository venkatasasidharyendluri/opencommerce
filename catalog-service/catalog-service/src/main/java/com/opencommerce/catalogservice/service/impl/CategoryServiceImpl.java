package com.opencommerce.catalogservice.service.impl;

import com.opencommerce.catalogservice.dto.request.CreateCategoryRequest;
import com.opencommerce.catalogservice.dto.request.UpdateCategoryRequest;
import com.opencommerce.catalogservice.dto.response.ApiResponse;
import com.opencommerce.catalogservice.dto.response.CategoryResponse;
import com.opencommerce.catalogservice.entity.Category;
import com.opencommerce.catalogservice.exception.CategoryAlreadyExistsException;
import com.opencommerce.catalogservice.exception.CategoryNotFoundException;
import com.opencommerce.catalogservice.mapper.CategoryMapper;
import com.opencommerce.catalogservice.repository.CategoryRepository;


import com.opencommerce.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public ApiResponse createCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByName(request.name())){
            throw new CategoryAlreadyExistsException("Category Already Exists");
        }
        Category category =  Category.builder()
                .name(request.name())
                .description(request.description())
                .build();

        categoryRepository.save(category);
        return new ApiResponse(true, "Category Created Successfully");
    }

    @Override
    public CategoryResponse getCategory(UUID uuid) {
        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category Not Found")
                );
        return categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findByActiveTrue()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public ApiResponse updateCategory(UUID uuid, UpdateCategoryRequest request) {
        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category Not Found")
                );
        category.setName(request.name());
        category.setDescription(request.description());
        category.setActive(request.active());
        categoryRepository.save(category);
        return new ApiResponse(true, "Category Updated Successfully");
    }

    @Override
    public ApiResponse deleteCategory(UUID uuid) {
        Category category = categoryRepository.findByUuid(uuid)
                .orElseThrow(
                        () -> new CategoryNotFoundException("Category Not Found")
                );
        category.setActive(false);
        categoryRepository.save(category);
        return new ApiResponse( true, "Category Successfully Deleted") ;
    }
}
