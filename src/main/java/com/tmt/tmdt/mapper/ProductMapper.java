package com.tmt.tmdt.mapper;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Product;

public interface ProductMapper {
    ProductResponseDto toProductResponseDto(Product product);
}
