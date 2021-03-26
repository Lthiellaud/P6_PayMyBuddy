package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceIT {
    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private ConnexionService connexionService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransferService transferService;

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

        beneficiary = new PMBUser();
        beneficiary.setUserId(1L);
        beneficiary.setEmail("beneficiary@mail.com");
        beneficiary.setBalance(40.0);

        transferDTO = new TransferDTO();
        transferDTO.setConnexionId(1L);
        transferDTO.setDescription("The transaction");
        transferDTO.setAmount(10.0);

        connexion = new Connexion();
        connexion.setConnexionId(1L);
        connexion.setBeneficiaryUser(beneficiary);
        connexion.setUser(user);
        connexion.setConnexionName("The connexion");

        Set<Connexion> connexions = new HashSet<>();
        connexions.add(connexion);
        user.setConnexions(connexions);
        beneficiary.setConnexions(connexions);


    }

    @Test
    public void registerTransferThrowExceptionOnUserSaveTest() throws Exception {
        //Response TransferDTO transferDTO, Connexion connexion
        when(pmbUserService.saveUser(user)).thenReturn(user);
        when(pmbUserService.saveUser(beneficiary)).thenThrow(RuntimeException.class);

        Response response = transferService.registerTransfer(transferDTO, connexion);

        assertThat(response).isEqualTo(Response.SAVE_KO);

    }


}
