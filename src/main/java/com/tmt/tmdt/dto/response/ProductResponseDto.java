package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


public class ProductResponseDto {
    private String name;
    private BigDecimal price;
    private float discountPercent;
    private String mainImageLink;
}
