package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter

public class TransactionResponseDto {

    private Long id;
    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String customerGender;


    private BigDecimal totalPrice;

    public TransactionResponseDto(Long id, String customerName, String customerAddress, String customerPhoneNumber, String customerGender, BigDecimal totalPrice, String status) {
        this.id = id;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerGender = customerGender;
        this.totalPrice = totalPrice;
    }

    //additional
    private Set<OrderResponseDto> orderItemList = new HashSet<>();
}
