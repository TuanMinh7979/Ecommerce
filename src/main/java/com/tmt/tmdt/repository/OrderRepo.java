package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("from Order o where o.transaction.id = :tid")
    Set<Order> getOrdersByTransactionId(@Param("tid") Long id);
}
