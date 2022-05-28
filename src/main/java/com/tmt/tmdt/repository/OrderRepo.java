package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Query("from Order o where o.transaction.id = :tid")
    Set<Order> getOrdersByTransactionId(@Param("tid") Long id);

    @Query(value = "select o.product_name as product_name, (o.unit_price* o.qty)  as order_price, t.status as status from orders o " +
            "join transactions t on o.transaction_id = t.id where status = 'SUCCESS' " +
            "group by product_name, order_price, status"

            , nativeQuery = true)
    List<Object[]> getProductTurnoverDStat();


    @Query(value = "select o.product_name as product_name, (o.unit_price* o.qty)  as order_price, t.status as status from orders o " +
            "join transactions t on o.transaction_id = t.id where status = 'SUCCESS' and t.paid_time > ?1 " +
            "group by product_name, order_price, status"
            , nativeQuery = true)
    List<Object[]> getProductTurnoverDStatFromDate(LocalDate fromDate);

    @Query(value = "select o.product_name as product_name, (o.unit_price* o.qty)  as order_price, t.status as status from orders o " +
            "join transactions t on o.transaction_id = t.id where status = 'SUCCESS' and t.paid_time < ?1 " +
            "group by product_name, order_price, status"
            , nativeQuery = true)
    List<Object[]> getProductTurnoverDStatToDate(LocalDate toDate);

    @Query(value = "select o.product_name as product_name, (o.unit_price* o.qty)  as order_price, t.status as status from orders o " +
            "join transactions t on o.transaction_id = t.id where status = 'SUCCESS' and t.paid_time between ?1 and ?2 " +
            "group by product_name, order_price, status"
            , nativeQuery = true)
    List<Object[]> getProductTurnoverDStatBetween(LocalDate fromDate, LocalDate toDate);


}
