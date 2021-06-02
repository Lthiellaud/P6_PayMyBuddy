package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.AccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.BankMovementService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.RibService;
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
 * To manage the URL /home/account (GET/POST)
 * To add or remove money from one PMB Account
 */
@Controller
public class AccountController {

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private BankMovementService bankMovementService;

    @Autowired
    private RibService ribService;

    @Autowired
    private AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @ModelAttribute("accountDTO")
    public AccountDTO getAccountDTOObject(){
        return new AccountDTO();
    }

    @GetMapping("/home/account")
    public String getAccountPage(Model model) {
        PMBUser user = pmbUserService.getCurrentUser();
        LOGGER.debug("get /home/account - Access ");
        model.addAttribute("balance", pmbUserService.getBalanceMessage(user));

        List<BankAccount> bankAccounts = ribService.getRibsByUser(user);
        LOGGER.debug("get /home/account - Number of RIBs : " + bankAccounts.size());
        model.addAttribute("bankAccounts", bankAccounts);

        List<BankMovement> bankMovements = bankMovementService.getMovements(bankAccounts);
        LOGGER.debug("get /home/account - Number of operations : " + bankMovements.size());
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        bankMovements.forEach(movement ->
                movement.setCaption(dFormat.format(movement.getMovementDate()) + " | "
                    + movement.getCaption()));

        model.addAttribute("bankMovements", bankMovements);
        return "accountPage";
    }

    @PostMapping("/home/account")
    public String manageAccount(@ModelAttribute("accountDTO") @Valid AccountDTO accountDTO,
                                      final BindingResult bindingResult, Model model) {
        LOGGER.debug("post /home/account - Selected BankAccount : " + accountDTO.getRibId()
                + ", selected operation : " + accountDTO.getDebitCredit()
                + ", amount : " + accountDTO.getAmount());
        if (bindingResult.hasErrors()) {
            LOGGER.debug("post /home/account - entry errors detected");
            return getAccountPage(model);
        }
        Response response = accountService.operateMovement(accountDTO);
        LOGGER.debug("post /home/account - operation result : " + response);
        switch (response) {
            case OK:
                model.addAttribute("accountDTO", new AccountDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case NOT_ENOUGH_MONEY:
                bindingResult.rejectValue("amount", "error.accountDTO", response.getMessage());
                break;
            case DATA_ISSUE:
            case SAVE_KO:
                model.addAttribute("message", response.getMessage());
                break;
            default:
        }
        LOGGER.debug("post /home/account - bindingResult : " + bindingResult.hasErrors());
        return getAccountPage(model);
    }

}
