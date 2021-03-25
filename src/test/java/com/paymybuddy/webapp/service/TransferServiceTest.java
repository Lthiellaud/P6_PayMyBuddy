package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceTest {
    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private ConnexionService connexionService;

    @MockBean
    private  TransactionService transactionService;

    @Autowired
    private TransferServiceImpl transferService;

    private static PMBUser user;
    private static PMBUser beneficiary;
    private static Connexion connexion;
    private TransferDTO transferDTO;

    @BeforeEach
    public void initTest() {
        user = new PMBUser();
        user.setUserId(1L);
        user.setEmail("user@mail.com");
        user.setBalance(40.0);
        Set<Connexion> connexions = new HashSet<>();
        connexions.add(connexion);
        user.setConnexions(connexions);

        beneficiary = new PMBUser();
        beneficiary.setUserId(1L);
        beneficiary.setEmail("beneficiary@mail.com");
        beneficiary.setBalance(40.0);
        beneficiary.setConnexions(connexions);

        transferDTO = new TransferDTO();
        transferDTO.setConnexionId(1L);
        transferDTO.setDescription("The transaction");
        transferDTO.setAmount(10.0);

        connexion = new Connexion();
        connexion.setConnexionId(1L);
        connexion.setBeneficiaryUser(beneficiary);
        connexion.setUser(user);
        connexion.setConnexionName("The connexion");

}

    @Test
    public void getUserConnexionTest() {
        List<Connexion> connexions = new ArrayList<>(user.getConnexions());
        when(pmbUserService.getCurrentUser()).thenReturn(user);
        when(connexionService.getConnexionsByUser(user)).thenReturn(connexions);

        assertThat(transferService.getUserConnexion().size()).isEqualTo(1);

    }

    @Test
    public void processTransferTest() {

        Optional<Connexion> c = Optional.of(connexion);

        when(connexionService.getById(1L)).thenReturn(c);
        //Response TransferDTO transferDTO

    }
    @Test
    public void registerTransferTest() throws Exception {
        //Response TransferDTO transferDTO, Connexion connexion
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(new Transaction());
        when(pmbUserService.saveUser(user)).thenReturn(user);
        when(pmbUserService.saveUser(beneficiary)).thenReturn(beneficiary);

        Response response = transferService.registerTransfer(transferDTO, connexion);

        assertThat(user.getBalance()).isEqualTo(30.0);
        assertThat(beneficiary.getBalance()).isEqualTo(50.0);
        assertThat(response).isEqualTo(Response.OK);

    }

    @Test
    public void registerTransferThrowExceptionOnTransferCreateTest() throws Exception {
        //Response TransferDTO transferDTO, Connexion connexion
        when(transactionService.createTransaction(any(Transaction.class))).thenThrow(RuntimeException.class);
        Response response = transferService.registerTransfer(transferDTO, connexion);
        assertThat(response).isEqualTo(Response.SAVE_KO);

    }
    @Test
    public void registerTransferThrowExceptionOnUserSaveTest() throws Exception {
        //Response TransferDTO transferDTO, Connexion connexion
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(new Transaction());
        when(pmbUserService.saveUser(user)).thenReturn(user);
        when(pmbUserService.saveUser(beneficiary)).thenThrow(RuntimeException.class);

        Response response = transferService.registerTransfer(transferDTO, connexion);

        assertThat(response).isEqualTo(Response.SAVE_KO);

    }


}
