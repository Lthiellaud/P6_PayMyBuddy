package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.AccountDTO;
import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RibService ribService;

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private BankMovementService bankMovementService;

    @Autowired
    private CallBankService callBankService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Response operateMovement(AccountDTO accountDTO) {
        Optional<BankAccount> r =ribService.getById(accountDTO.getRibId());
        if (!r.isPresent()) {
            LOGGER.debug(Response.DATA_ISSUE + " for account operation : " + accountDTO.getRibId()
                    + ", selected operation : " + accountDTO.getDebitCredit()
                    + ", amount : " + accountDTO.getAmount());
            return Response.DATA_ISSUE;
        }
        BankAccount bankAccount = r.get();

        if (accountDTO.getAmount()  * accountDTO.getDebitCredit() + bankAccount.getUser().getBalance() < 0) {
            LOGGER.debug(Response.NOT_ENOUGH_MONEY + " for account operation : " + accountDTO.getRibId()
                    + ", selected operation : " + accountDTO.getDebitCredit()
                    + ", amount : " + accountDTO.getAmount());
            return Response.NOT_ENOUGH_MONEY;
        }
        Response response = Response.SAVE_KO;
        try {
            response = registerMovement(accountDTO, bankAccount);
        } catch (Exception e) {
            LOGGER.debug(response + " for account operation : " + accountDTO.getRibId()
                    + ", selected operation : " + accountDTO.getDebitCredit()
                    + ", amount : " + accountDTO.getAmount());
            return response;
        }
        LOGGER.debug(response + " for account operation : " + accountDTO.getRibId()
                + ", selected operation : " + accountDTO.getDebitCredit()
                + ", amount : " + accountDTO.getAmount());
        return response;
    }

    @Override
    @Transactional
    public Response registerMovement(AccountDTO accountDTO, BankAccount bankAccount) {
        //Set the new bank movement
        BankMovement bankMovement = new BankMovement();
        bankMovement.setAmount(accountDTO.getAmount() * accountDTO.getDebitCredit());
        bankMovement.setMovementDate(new Date());
        bankMovement.setCaption((accountDTO.getDebitCredit() == -1 ? "Get my money" : "Fund my account"));
        bankMovement.setBankAccount(bankAccount);
        bankMovementService.createMovement(bankMovement);

        //send the movement to the bank and get a response
        //untreated for the prototype of the application
        BankExchangeDTO bankExchangeDTO = new BankExchangeDTO();
        if (callBankService.sendBankMovement(bankExchangeDTO) != Response.OK) {
            throw new RuntimeException("A problem occurred during the exchange with your bank");
        }

        //update the user
        pmbUserService.updateUserBalance(bankAccount.getUser(), bankMovement.getAmount());

        return Response.OK;
    }
}
