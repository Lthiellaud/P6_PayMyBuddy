package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TransferController {

    @Autowired
    private TransferServiceImpl transferService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/home/transfer")
    public String getTransferPage(Model model, Principal principal) {
        List<Connexion> connexions = transferService.getUserConnexion();
        System.out.println(connexions.size());
        List<Transaction> transactions = transactionService.getTransactions(connexions);
        System.out.println(transactions.size());
        model.addAttribute("connexions", connexions);
        model.addAttribute("transactions", transactions);
        return "transferPage";
    }
}
