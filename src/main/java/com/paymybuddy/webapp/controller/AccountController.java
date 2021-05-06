package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.OperationDTO;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.BankMovementService;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
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
public class AccountController {

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private BankMovementService bankMovementService;

    @Autowired
    private PMBSharedService pmbSharedService;

    @Autowired
    private AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @ModelAttribute("operationDTO")
    public OperationDTO getOperationDTOObject(){
        return new OperationDTO();
    }

    @GetMapping("/home/account")
    public String getAccountPage(Model model) {
        PMBUser user = pmbUserService.getCurrentUser();
        model.addAttribute("balance", pmbUserService.getBalanceMessage(user));

        List<Rib> ribs = pmbSharedService.getUserRib();
        LOGGER.debug("get /home/account - Number of RIBs : " + ribs.size());
        model.addAttribute("ribs", ribs);

        List<BankMovement> bankMovements = bankMovementService.getMovements(ribs);
        LOGGER.debug("get /home/account - Number of operations : " + bankMovements.size());

        model.addAttribute("bankMovements", bankMovements);
        return "accountPage";
    }

    @PostMapping("/home/account")
    public String manageAccount(@ModelAttribute("operationDTO") @Valid OperationDTO operationDTO,
                                      final BindingResult bindingResult, Model model) {
        LOGGER.debug("post /home/account - Selected Rib : " + operationDTO.getRibId()
                + ", selected operation : " + operationDTO.getDebitCredit()
                + ", amount : " + operationDTO.getAmount());
        if (bindingResult.hasErrors()) {
            LOGGER.debug("post /home/account - entry errors detected");
            return getAccountPage(model);
        }
        Response response = accountService.operateMovement(operationDTO);
        LOGGER.debug("post /home/account - operation result : " + response);
        switch (response) {
            case OK:
                model.addAttribute("operationDTO", new OperationDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case NOT_ENOUGH_MONEY:
                bindingResult.rejectValue("amount", "error.operationDTO", response.getMessage());
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
