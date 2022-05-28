package com.tmt.tmdt.mapper.impl;

import com.tmt.tmdt.dto.response.OrderResponseDto;
import com.tmt.tmdt.entities.Order;
import com.tmt.tmdt.mapper.OrderMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setAvatar(order.getAvatar());
        dto.setUnitPrice(order.getUnitPrice());
        dto.setSalePercent(order.getSalePercent());
        dto.setQty(order.getQty());
        dto.setOptions(order.getOptions());
        dto.setProductName(order.getProductName());
        return dto;

    }
}
