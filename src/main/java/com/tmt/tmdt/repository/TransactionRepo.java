package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {


    @Query("from Transaction t join fetch t.orderItemList where t.customerPhoneNumber = :phonenumber")
    List<Transaction> getTransactionWithOrdersByPhoneNumber(String phoneNumber);
}
