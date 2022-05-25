package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {


    @Query("select t from Transaction t left join fetch t.orderItemList o where t.customerPhoneNumber = :phoneNumber and t.status <> 'SUCCESS' group by t.id, o.id ")
    Set<Transaction> getCurTransactionWithOrdersByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
