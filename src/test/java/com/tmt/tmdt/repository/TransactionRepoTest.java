package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class TransactionRepoTest {

    @Autowired
    public TransactionRepo transactionRepo;

    @Test
    public void addMasterRole() {

        Transaction transaction = transactionRepo.getTransactionWithOrders(Long.valueOf(14)).get();

//        List<Transaction> list = transactionRepo.findAll();
//        for (Transaction tri : list) {
//            System.out.println(tri.getId());
//        }
        assertThat(transaction.getId() > 0);

    }

}