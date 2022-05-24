package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.Order;
import com.tmt.tmdt.repository.OrderRepo;
import com.tmt.tmdt.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;

    @Override
    public Order save(Order order) {
        return orderRepo.save(order);
    }
}
