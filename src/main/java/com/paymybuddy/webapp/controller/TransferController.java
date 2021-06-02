package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
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
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * To manage the URL /home/transfer (GET/POST)
 * To process transfer to a buddy (via a connexion)
 */
@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private PMBUserService pmbUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @ModelAttribute("transfer")
    public TransferDTO getTransferDTOObject(){
        return new TransferDTO();
    }

    @GetMapping("/home/transfer")
    public String getTransferPage(Model model) {
        PMBUser user = pmbUserService.getCurrentUser();
        LOGGER.debug("get /home/transfer - Access");
        List<Connexion> connexions = connexionService.getConnexionsByUser(user);
        LOGGER.debug("get /home/transfer - Number of connexions: " + connexions.size());
        model.addAttribute("connexions", connexions);

        List<Transaction> transactions = transactionService.getTransactions(connexions);
        LOGGER.debug("get /home/transfer - Number of transactions: " + transactions.size());
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        transactions.forEach(transaction ->
                transaction.setDescription(dFormat.format(transaction.getTransactionDate())
                    + " | " + transaction.getDescription()));
        model.addAttribute("transactions", transactions);

        return "transferPage";
    }

    @PostMapping("/home/transfer")
    public String transferMoney(@ModelAttribute("transfer") @Valid TransferDTO transfer,
                                      final BindingResult bindingResult, Model model) {
        LOGGER.debug("post /home/transfer for transfer: " + transfer.getConnexionId()
                + ", description: " + transfer.getDescription()
                + ", amount " + transfer.getAmount());
        if (bindingResult.hasErrors()) {
            return getTransferPage(model);
        }
        Response response= transferService.processTransfer(transfer);
        LOGGER.debug("post /home/transfer - result: " + response);
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
        LOGGER.debug("bindingResult : " + bindingResult.hasErrors());
        return getTransferPage(model);
    }

}
