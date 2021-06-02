package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.DTO.BankAccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
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
import java.util.List;


/**
 * To manage the URL /home/bankAccount (GET/POST)
 * To add rib (bank account) to one PMB Account
 */
@Controller
public class RibController {

    @Autowired
    private RibService ribService;
    
    @Autowired
    private PMBUserService pmbUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RibController.class);

    @ModelAttribute("bankAccountDTO")
    public BankAccountDTO getBankAccountDTOObject(){
        return new BankAccountDTO();
    }

    @GetMapping("/home/bankAccount")
    public String getRibPage(Model model) {
        PMBUser user = pmbUserService.getCurrentUser();
        LOGGER.debug("get /home/bankAccount - Access");
        List<BankAccount> bankAccounts = ribService.getRibsByUser(user);
        LOGGER.debug("get /home/bankAccount - Number of rib : " + bankAccounts.size());
        model.addAttribute("bankAccounts", bankAccounts);
        return "bankAccountPage";
    }
    
    @PostMapping("/home/bankAccount")
    public String addRib(@ModelAttribute ("bankAccountDTO") @Valid BankAccountDTO bankAccountDTO,
                               final BindingResult bindingResult, Model model) {
        LOGGER.debug("post /home/bankAccount - BankAccount name: " + bankAccountDTO.getRibName()
                + ", account owner:" + bankAccountDTO.getAccountOwner() + ""
                + ", RIB:" + bankAccountDTO.getCountryCode()
                + bankAccountDTO.getBankCode()
                + bankAccountDTO.getBranchCode()
                + bankAccountDTO.getAccountCode()
                + bankAccountDTO.getKey()
                + ", BIC:" + bankAccountDTO.getBic());
        if (bindingResult.hasErrors()) {
            return getRibPage(model);
        }
        Response response = ribService.processRib(bankAccountDTO);
        LOGGER.debug("post /home/bankAccount - operation result : " + response);
        switch (response) {
            case OK:
                model.addAttribute("bankAccountDTO", new BankAccountDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case EXISTING_RIB_NAME:
                bindingResult.rejectValue("ribName", "error.bankAccountDTO", response.getMessage());
                break;
            case EXISTING_IBAN:
            case IBAN_BIC_KO:
            case SAVE_KO:
                model.addAttribute("message", response.getMessage());
                break;
            default:
        }
        LOGGER.debug("post /home/bankAccount - bindingResult : " + bindingResult.hasErrors());
        return getRibPage(model);
    }
    
}
