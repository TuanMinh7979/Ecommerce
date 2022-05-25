package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Transaction;

import java.util.List;

public interface TransactionService {
    List<TransactionResponseDto> getCurTransactionWithOrdersByPhoneNumber(String phoneNumber);

    Transaction save(Transaction transaction);

    Transaction getTransactionWithOrders(Long id);

    Transaction getTransaction(Long id);

    void deleteById(Long id);
}
