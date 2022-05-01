package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


public class ProductResponseDto {
    private String name;
    private BigDecimal price;
    private float discountPercent;
    private String mainImageLink;
    private String code;
//    private List<String> imageDetailLinks = new ArrayList<>();

}
