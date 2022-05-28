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

    private String productName;
    private BigDecimal unitPrice;

    private int qty;

    private int salePercent;

    private String options;

    private String avatar;

//    addtionnal attribute aboutproduct


    public OrderResponseDto(String productName, BigDecimal unitPrice, int qty, int salePercent, String options, String avatar) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.salePercent = salePercent;
        this.options = options;
        this.avatar = avatar;
    }

}
