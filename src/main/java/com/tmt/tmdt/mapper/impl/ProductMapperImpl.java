package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.repository.ProductRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponseDto toProductResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setDiscountPercent(product.getDiscountPercent() != null ? product.getDiscountPercent() : 0);
        productResponseDto.setMainImageLink(product.getMainImageLink());
        productResponseDto.setCode(product.getCode());
//        productResponseDto.setImageDetailLinks(product.getImages().stream()
//                .map(img -> img.getLink()).collect(Collectors.toList()));
        return productResponseDto;
    }
}
