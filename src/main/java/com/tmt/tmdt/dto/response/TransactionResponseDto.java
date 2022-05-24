package com.tmt.tmdt.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor

@Getter
@Setter

public class TransactionResponseDto {

    private String customerName;
    private String customerAddress;
    private String customerPhoneNumber;
    private String customerGender;


    private BigDecimal totalPrice;

}
