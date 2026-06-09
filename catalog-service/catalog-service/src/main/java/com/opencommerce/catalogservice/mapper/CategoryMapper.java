package com.opencommerce.catalogservice.mapper;

import com.opencommerce.catalogservice.dto.response.CategoryResponse;
import com.opencommerce.catalogservice.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryMapper {
    public CategoryResponse toResponse(Category category){
        return new CategoryResponse(
                category.getUuid(),
                category.getName(),
                category.getDescription(),
                category.getActive()
        );
    }
}
