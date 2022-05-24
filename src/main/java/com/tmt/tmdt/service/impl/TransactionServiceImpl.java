package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.repository.TransactionRepo;
import com.tmt.tmdt.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;


    @Override
    public List<Transaction> getTransactionWithOrdersByPhoneNumber(String phoneNumber) {

        return transactionRepo.getTransactionWithOrdersByPhoneNumber(phoneNumber);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }
}
