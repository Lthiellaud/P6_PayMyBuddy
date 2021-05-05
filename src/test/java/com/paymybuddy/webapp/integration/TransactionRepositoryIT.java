package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class TransactionRepositoryIT {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ConnexionRepository connexionRepository;

    private static List<Transaction> transactions;

    @Test
    public void findAllByConnexionOrderByTransactionDateDescTest() {
        List<Long> connexionIds = Arrays.asList(1L, 2L);
        List<Connexion> connexions = connexionRepository.findAllById(connexionIds);
        transactions = new ArrayList<>();

        transactions = transactionRepository
                .findAllByConnexionInOrderByTransactionDateDesc(connexions);

        assertThat(transactions.size()).isEqualTo(2);
        assertThat(transactions.get(0).getConnexion().getConnexionId()).isEqualTo(2L);
        assertThat(transactions.get(1).getConnexion().getConnexionId()).isEqualTo(1L);
    }

}
