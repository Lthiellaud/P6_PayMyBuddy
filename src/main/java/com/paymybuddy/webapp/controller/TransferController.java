package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class TransferController {

    @Autowired
    private TransferServiceImpl transferService;

    @Autowired
    private TransactionService transactionService;

    @RolesAllowed("USER")
    @GetMapping("/home/transfer")
    public String getTransferPage(Model model) {
        List<Connexion> connexions = transferService.getUserConnexion();
        System.out.println(connexions.size());
        List<Transaction> transactions = transactionService.getTransactions(connexions);
        System.out.println(transactions.size());
        model.addAttribute("connexions", connexions);
        model.addAttribute("transactions", transactions);
        model.addAttribute("transfer", new TransferDTO());
        return "transferPage";
    }

    @RolesAllowed("USER")
    @PostMapping("/home/transfer")
    public ModelAndView transferMoney(@ModelAttribute TransferDTO transfer) {
        System.out.println(transfer.getConnexionId() + " " + transfer.getDescription()
                + " " + transfer.getAmount());
        transferService.processTransfer(transfer);
        return new ModelAndView("/home");
    }

}
