package com.tmt.tmdt.mapper;

import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Transaction;

public interface TransactionMapper  {
    TransactionResponseDto toDto(Transaction transaction);

}
