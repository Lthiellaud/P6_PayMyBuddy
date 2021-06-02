package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions(List<Connexion> connexions) {
        return transactionRepository
                .findAllByConnexionInOrderByTransactionDateDesc(connexions);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteAllTransaction() {
        transactionRepository.deleteAll();
    }
}
