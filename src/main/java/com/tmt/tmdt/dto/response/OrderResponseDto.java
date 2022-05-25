package com.tmt.tmdt.dto.response;

import com.tmt.tmdt.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Setter
@Getter

@NoArgsConstructor
public class OrderResponseDto {

    private BigDecimal unitPrice;

    private int qty;

    private int salePercent;

    private String options;

    private String avatar;

//    addtionnal attribute aboutproduct


    public OrderResponseDto(BigDecimal unitPrice, int qty, int salePercent, String options, String avatar) {
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.salePercent = salePercent;
        this.options = options;
        this.avatar = avatar;
    }

    private String productName;
}
