package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * gives access to Transaction records.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * To retrieve a list of transactions from a list of connexions.
     * @param connexions The list of connexions
     * @return the list of transactions
     */
    List<Transaction> findAllByConnexionInOrderByTransactionDateDesc(List<Connexion> connexions);

}
