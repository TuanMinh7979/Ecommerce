package com.tmt.tmdt.mapper;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;

public interface CategoryMapper {
    CategoryResponseDto toCategoryResponseDto(Category category);
}
