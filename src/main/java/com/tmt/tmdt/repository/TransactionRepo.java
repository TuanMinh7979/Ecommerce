package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {


    @Query("select t from Transaction t left join fetch t.orderItemList o where t.customerPhoneNumber = :phoneNumber and t.status <> 'SUCCESS'")
    Set<Transaction> getCurTransactionWithOrdersByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("select t from Transaction t left join fetch t.orderItemList where t.id= :id")
    Optional<Transaction> getTransactionWithOrders(@Param("id") Long id);

    @Query("select t from Transaction t left join fetch t.orderItemList o join fetch o.product where t.id = :id")
    Optional<Transaction> getTransactionWithOrderWithProduct(@Param("id") Long id);


}
