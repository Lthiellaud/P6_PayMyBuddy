package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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


        beneficiary = new PMBUser();
        beneficiary.setUserId(2L);
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
        connexion.setPmbUser(user);
        connexion.setConnexionName("The connexion");

        //ajouter la connexion à user
        connexions.add(connexion);
        user.setConnexions(connexions);
}

    @Test
    public void processTransferTest() throws Exception {

        Optional<Connexion> c = Optional.of(connexion);
        when(connexionService.getById(1L)).thenReturn(c);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(new Transaction());
        when(pmbUserService.updateUserBalance(user, -10.0)).thenReturn(user);
        when(pmbUserService.updateUserBalance(beneficiary, 10.0)).thenReturn(beneficiary);

        Response response = transferService.processTransfer(transferDTO);

        verify(connexionService, times(1)).getById(1L);
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
        verify(pmbUserService, times(1)).updateUserBalance(user, -10.0);
        verify(pmbUserService, times(1)).updateUserBalance(beneficiary, 10.0);
        assertThat(response).isEqualTo(Response.OK);

    }

    @Test
    public void processTransferSAVE_KOTest() throws Exception {

        Optional<Connexion> c = Optional.of(connexion);
        when(connexionService.getById(1L)).thenReturn(c);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(new Transaction());
        when(pmbUserService.updateUserBalance(user, -10.0)).thenReturn(user);
        when(pmbUserService.updateUserBalance(beneficiary, 10.0)).thenThrow(RuntimeException.class);

        Response response = transferService.processTransfer(transferDTO);

        verify(connexionService, times(1)).getById(1L);
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
        verify(pmbUserService, times(1)).updateUserBalance(user, -10.0);
        verify(pmbUserService, times(1)).updateUserBalance(beneficiary, 10.0);
        assertThat(response).isEqualTo(Response.SAVE_KO);

    }

    @Test
    public void processTransferWhenConnexionIsNotFoundShouldReturnDATA_ISSUE() throws Exception {
        //Response TransferDTO transferDTO, Connexion connexion
        when(connexionService.getById(1L)).thenReturn(Optional.empty());
        Response response = transferService.processTransfer(transferDTO);
        assertThat(response).isEqualTo(Response.DATA_ISSUE);

    }
    @Test
    public void processTransferWithAmount50ShouldReturnNOT_ENOUGH_MONEY() throws Exception {
        transferDTO.setAmount(50.0);
        Optional<Connexion> c = Optional.of(connexion);
        when(connexionService.getById(1L)).thenReturn(c);

        Response response = transferService.processTransfer(transferDTO);

        assertThat(response).isEqualTo(Response.NOT_ENOUGH_MONEY);

    }


}
