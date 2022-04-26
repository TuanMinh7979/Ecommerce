package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryResponseDto toCategoryResponseDto(Category category) {
        if (category == null) return null;
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setCode(category.getCode());
        categoryResponseDto.setParentId(category.getParent().getId());
        return categoryResponseDto;
    }
}
