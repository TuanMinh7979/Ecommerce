package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.mapper.TransactionMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapperImpl implements TransactionMapper {
    @Override
    public TransactionResponseDto toDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setCustomerName(transaction.getCustomerName());
        dto.setCustomerAddress(transaction.getCustomerAddress());
        dto.setCustomerGender(transaction.getCustomerGender());
        dto.setCustomerPhoneNumber(transaction.getCustomerPhoneNumber());
        dto.setTotalPrice(transaction.getTotalPrice());
        return dto;
    }
}
