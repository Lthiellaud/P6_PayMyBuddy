package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByConnexionInOrderByTransactionDateDesc(List<Connexion> connexionIds);

}
