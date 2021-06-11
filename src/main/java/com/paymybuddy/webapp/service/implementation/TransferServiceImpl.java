package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.TransferService;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.paymybuddy.webapp.model.constants.Commission.COMMISSION_PC;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private TransactionService transactionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Override
    public Response processTransfer(TransferDTO transferDTO) {
        Optional<Connexion> c = connexionService.getById(transferDTO.getConnexionId());
        if (!c.isPresent()) {
            LOGGER.debug(Response.DATA_ISSUE + " for transfer: " + transferDTO.getConnexionId()
                    + ", description: " + transferDTO.getDescription()
                    + ", amount " + transferDTO.getAmount());
            return Response.DATA_ISSUE;
        }
        Connexion connexion = c.get();
        //check if there is enough money on the user account
        if (connexion.getPmbUser().getBalance() <
                Precision.round(transferDTO.getAmount() * (100 + COMMISSION_PC)/100, 2)) {
            LOGGER.debug(Response.NOT_ENOUGH_MONEY + " for transfer: " + transferDTO.getConnexionId()
                    + ", description: " + transferDTO.getDescription()
                    + ", amount " + transferDTO.getAmount());
            return Response.NOT_ENOUGH_MONEY;
        }
        Response response = Response.SAVE_KO;
        try {
            response = registerTransfer(transferDTO, connexion);
        } catch (Exception e) {
            LOGGER.debug(response + " for transfer: " + transferDTO.getConnexionId()
                    + ", description: " + transferDTO.getDescription()
                    + ", amount " + transferDTO.getAmount());
            return response;
        }
        LOGGER.debug(response + " for transfer: " + transferDTO.getConnexionId()
                + ", description: " + transferDTO.getDescription()
                + ", amount " + transferDTO.getAmount());
        return response;

    }

    @Override
    @Transactional
    public Response registerTransfer(TransferDTO transferDTO, Connexion connexion) throws Exception {
        //set transaction
        Transaction transaction = new Transaction();
        transaction.setConnexion(connexion);
        transaction.setDescription(transferDTO.getDescription());
        transaction.setAmount(transferDTO.getAmount());
        transaction.setCommissionPc(COMMISSION_PC);
        transaction.setTransactionDate(new Date());
        transactionService.createTransaction(transaction);

        //update user balance (subtract amount * (1 + commission%)
        pmbUserService.updateUserBalance(connexion.getPmbUser(),
                Precision.round(transferDTO.getAmount() * (-1) * (100 + COMMISSION_PC)/100, 2));

        //update beneficiary balance (add amount)
        pmbUserService.updateUserBalance(connexion.getBeneficiaryUser(), transferDTO.getAmount());

        return Response.OK;

    }

}
