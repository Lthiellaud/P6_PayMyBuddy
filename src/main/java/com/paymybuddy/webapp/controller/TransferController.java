package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;


@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PMBSharedService pmbSharedService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @ModelAttribute("transfer")
    public TransferDTO getTransferDTOObject(){
        return new TransferDTO();
    }

    @GetMapping("/home/transfer")
    public String getTransferPage(Model model) {
        List<Connexion> connexions = pmbSharedService.getUserConnexion();
        System.out.println(connexions.size());
        List<Transaction> transactions = transactionService.getTransactions(connexions);
        System.out.println(transactions.size());
        model.addAttribute("connexions", connexions);
        model.addAttribute("transactions", transactions);
        return "transferPage";
    }

    @PostMapping("/home/transfer")
    public String transferMoney(@ModelAttribute("transfer") @Valid TransferDTO transfer,
                                      final BindingResult bindingResult, Model model) {
        System.out.println(transfer.getConnexionId() + " " + transfer.getDescription()
                + " " + transfer.getAmount());
        if (bindingResult.hasErrors()) {
            return getTransferPage(model);
        }
        Response response= transferService.processTransfer(transfer);
        System.out.println(response);
        switch (response) {
            case OK:
                model.addAttribute("transfer", new TransferDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case NOT_ENOUGH_MONEY:
                bindingResult.rejectValue("amount", "error.transfer", response.getMessage());
                break;
            case DATA_ISSUE:
            case SAVE_KO:
                model.addAttribute("message", response.getMessage());
                break;
            default:
        }
        System.out.println("bindingResult : " + bindingResult.hasErrors());
        return getTransferPage(model);
    }

}
