package com.tmt.tmdt.dto.response;

import com.tmt.tmdt.entities.Product;

import java.math.BigDecimal;

public class OrderResponseDto {
    private String productName;

    private BigDecimal unitPrice;

    private int qty;

    private int salePercent;

    private String options;

    private String avatar;
}
