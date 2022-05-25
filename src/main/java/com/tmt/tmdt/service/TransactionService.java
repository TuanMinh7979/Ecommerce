package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDto> getCurTransactionWithOrdersByPhoneNumber(String phoneNumber);

    Transaction save(Transaction transaction);
}
