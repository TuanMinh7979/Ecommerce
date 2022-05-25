package com.tmt.tmdt.mapper;

import com.tmt.tmdt.dto.response.OrderResponseDto;
import com.tmt.tmdt.dto.response.TransactionResponseDto;
import com.tmt.tmdt.entities.Order;
import com.tmt.tmdt.entities.Transaction;

public interface OrderMapper {
    OrderResponseDto toDto(Order order);

}
