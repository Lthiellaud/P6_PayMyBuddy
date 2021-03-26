package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;


@Controller
public class TransferController {

    @Autowired
    private TransferServiceImpl transferService;

    @Autowired
    private TransactionService transactionService;

    @ModelAttribute("transfer")
    public TransferDTO getTransferDTOObject(){
        return new TransferDTO();
    }

    @RolesAllowed("USER")
    @GetMapping("/home/transfer")
    public String getTransferPage(Model model) {
        List<Connexion> connexions = transferService.getUserConnexion();
        System.out.println(connexions.size());
        List<Transaction> transactions = transactionService.getTransactions(connexions);
        System.out.println(transactions.size());
        model.addAttribute("connexions", connexions);
        model.addAttribute("transactions", transactions);
        return "transfer";
    }

    @RolesAllowed("USER")
    @PostMapping("/home/transfer")
    public String transferMoney(@ModelAttribute("transfer") @Valid TransferDTO transfer,
                                      final BindingResult bindingResult, Model model) {
        System.out.println(transfer.getConnexionId() + " " + transfer.getDescription()
                + " " + transfer.getAmount());
        if (bindingResult.hasErrors()) {
            //model.addAttribute("transfer", transfer);
            return "transfer";
        }
        Response response= transferService.processTransfer(transfer);
        System.out.println(response);
        return "redirect:transfer";
    }

}
