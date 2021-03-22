package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions(List<Connexion> connexions) {
        List<Transaction> transactions = transactionRepository
                .findAllByConnexionInOrderByTransactionDateDesc(connexions);

        return transactions;
    }

    @Override
    public Optional<Transaction> createTransaction() {
        return Optional.empty();
    }
}
