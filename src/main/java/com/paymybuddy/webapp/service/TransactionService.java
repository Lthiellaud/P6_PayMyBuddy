package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions(List<Connexion> connexions);

    List<Transaction> getAllTransactions();

    Transaction createTransaction(Transaction transaction) throws Exception;

    void deleteAllTransaction();
}
