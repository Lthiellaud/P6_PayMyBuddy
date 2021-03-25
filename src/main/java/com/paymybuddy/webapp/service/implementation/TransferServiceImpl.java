package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
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

    @Override
    public List<Connexion> getUserConnexion() {
        PMBUser user = pmbUserService.getCurrentUser();
        List<Connexion> connexions = connexionService.getConnexionsByUser(user);

        return connexions;
    }

    @Override
    public Response processTransfer(TransferDTO transferDTO) {
        Response response = null;
        Optional<Connexion> c = connexionService.getById(transferDTO.getConnexionId());
        if (c.isPresent()) {
            Connexion connexion = c.get();
            //check if there is enough money on the user account
            if (connexion.getUser().getBalance() < transferDTO.getAmount()) {
                response = Response.NOT_ENOUGH_MONEY;
            } else {
                response = registerTransfer(transferDTO, connexion);
            }
        }
        return response;
    }

    @Override
    @Transactional
    public Response registerTransfer(TransferDTO transferDTO, Connexion connexion) {
        Response response = Response.SAVE_KO;
        //set transaction
        Transaction transaction = new Transaction();
        transaction.setConnexion(connexion);
        transaction.setDescription(transferDTO.getDescription());
        transaction.setAmount(transferDTO.getAmount());
        transaction.setMonetizationPC(COMMISSION_PC);
        transaction.setTransactionDate(new Date());
        try {
            transactionService.createTransaction(transaction);
        } catch (Exception e) {
            return response;
        }

        //update user balance
        PMBUser user = connexion.getUser();
        user.setBalance(user.getBalance() - transferDTO.getAmount());
        try {
            pmbUserService.saveUser(user);
        } catch (Exception e) {
            return response;
        }

        //update beneficiary balance
        PMBUser beneficiary = connexion.getBeneficiaryUser();
        beneficiary.setBalance(beneficiary.getBalance() + transferDTO.getAmount());
        try {
            pmbUserService.saveUser(beneficiary);
        } catch (Exception e) {
            return response;
        }
        response = Response.OK;
        return response;

    }

}
