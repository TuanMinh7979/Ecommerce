package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class TransactionRepoTest {

    @Autowired
    public TransactionRepo transactionRepo;


    @Test
    public void addMasterRole() {
        Transaction tra = new Transaction();
        tra.setPaidTime(LocalDateTime.now());
        transactionRepo.save(tra);


    }


}