package com.tmt.tmdt.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor

@Getter
@Setter


public class ProductResponseDto {
    private String name;
    private BigDecimal price;
    private Float discountPercent;
    private String mainImageLink;
    private String code;
//    private List<String> imageDetailLinks = new ArrayList<>();


    public ProductResponseDto(String name, BigDecimal price, Float discountPercent, String mainImageLink, String code) {
        this.name = name;
        this.price = price;
        this.discountPercent = discountPercent;
        this.mainImageLink = mainImageLink;
        this.code = code;
    }


}
