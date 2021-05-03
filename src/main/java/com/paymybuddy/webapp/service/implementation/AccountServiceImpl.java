package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.BankMovementDTO;
import com.paymybuddy.webapp.model.DTO.OperationDTO;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.*;
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

    @Override
    public Response operateMovement(OperationDTO operationDTO) {
        Optional<Rib> r =ribService.getById(operationDTO.getRibId());
        if (!r.isPresent()) {
            return Response.DATA_ISSUE;
        }
        Rib rib = r.get();
        int sign = operationDTO.getDebitCredit() == 2 ? (-1) : 1;
        System.out.println("signe" + sign);
        operationDTO.setAmount(operationDTO.getAmount() * sign);
        System.out.println("Montant signé : " + operationDTO.getAmount());
        if (operationDTO.getAmount() + rib.getUser().getBalance() < 0) {
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
        BankMovement bankMovement = new BankMovement();
        bankMovement.setAmount(operationDTO.getAmount());
        bankMovement.setCaption(operationDTO.getDebitCredit() == 2?"Get my money" : "Fund my account");
        bankMovement.setMovementDate(new Date());
        bankMovement.setRib(rib);
        bankMovementService.createMovement(bankMovement);

        //send the movement to the bank and get a response
        //Non traité dans la maquette
        BankMovementDTO bankMovementDTO = new BankMovementDTO();
        if (callBankService.sendBankMovement(bankMovementDTO) != Response.OK) {
            throw new RuntimeException();
        }

        //update the user
        pmbUserService.updateUserBalance(rib.getUser(), operationDTO.getAmount());

        return Response.OK;
    }
}
