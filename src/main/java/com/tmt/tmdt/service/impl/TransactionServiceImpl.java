package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.dto.response.OrderResponseDto;
import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Order;
import com.tmt.tmdt.entities.Transaction;
import com.tmt.tmdt.mapper.OrderMapper;
import com.tmt.tmdt.mapper.TransactionMapper;
import com.tmt.tmdt.repository.TransactionRepo;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final OrderMapper orderMapper;
    private final TransactionMapper transactionMapper;


    @Override
    public List<TransactionResponseDto> getCurTransactionWithOrdersByPhoneNumber(String phoneNumber) {

        Set<Transaction> transactions = transactionRepo.getCurTransactionWithOrdersByPhoneNumber(phoneNumber);

        List<TransactionResponseDto> transactionResponseDtos = new ArrayList<>();
        for (Transaction trai : transactions) {
            Set<Order> orderSet = trai.getOrderItemList();
            Set<OrderResponseDto> orderResDtoSet = new HashSet<>();
            for (Order orderi : orderSet) {
                OrderResponseDto orderResDtoi = orderMapper.toDto(orderi);
//                orderi.getProduct().getId();
                orderResDtoSet.add(orderResDtoi);
            }
            TransactionResponseDto traResDtoi = transactionMapper.toDto(trai);
            traResDtoi.setOrderItemList(orderResDtoSet);

            transactionResponseDtos.add(traResDtoi);
        }
        return transactionResponseDtos;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }
}
