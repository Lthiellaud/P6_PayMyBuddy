package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.DTO.OperationDTO;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    @Override
    public Response operateMovement(OperationDTO operationDTO) {
        Optional<Rib> r =ribService.getById(operationDTO.getRibId());
        if (!r.isPresent()) {
            return Response.DATA_ISSUE;
        }
        Rib rib = r.get();

        if (operationDTO.getAmount()  * operationDTO.getDebitCredit() + rib.getUser().getBalance() < 0) {
            return Response.NOT_ENOUGH_MONEY;
        }
        Response response = Response.SAVE_KO;
        try {
            response = registerMovement(operationDTO, rib);
        } catch (Exception e) {
            return response;
        }

        return response;
    }

    @Override
    @Transactional
    public Response registerMovement(OperationDTO operationDTO, Rib rib) {
        //Set the new bank movement
        SimpleDateFormat dFormat = new SimpleDateFormat("dd-MM-yyyy");
        BankMovement bankMovement = new BankMovement();
        bankMovement.setAmount(operationDTO.getAmount() * operationDTO.getDebitCredit());
        bankMovement.setMovementDate(new Date());
        bankMovement.setCaption(dFormat.format(bankMovement.getMovementDate()) + " - "
                + (operationDTO.getDebitCredit() == -1 ? "Get my money" : "Fund my account"));
        bankMovement.setRib(rib);
        bankMovementService.createMovement(bankMovement);

        //send the movement to the bank and get a response
        //untreated for the prototype of the application
        BankExchangeDTO bankExchangeDTO = new BankExchangeDTO();
        if (callBankService.sendBankMovement(bankExchangeDTO) != Response.OK) {
            throw new RuntimeException("A problem occurred during the exchange with your bank");
        }

        //update the user
        pmbUserService.updateUserBalance(rib.getUser(), bankMovement.getAmount());

        return Response.OK;
    }
}
