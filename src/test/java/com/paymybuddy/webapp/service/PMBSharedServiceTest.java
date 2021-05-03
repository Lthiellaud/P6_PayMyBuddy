package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.implementation.PMBSharedServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PMBSharedServiceTest {

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private ConnexionService connexionService;

    @MockBean
    private  TransactionService transactionService;

    @Autowired
    private PMBSharedServiceImpl pmbSharedService;

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
    public void getUserConnexionTest() {
        List<Connexion> connexions = new ArrayList<>(user.getConnexions());
        when(pmbUserService.getCurrentUser()).thenReturn(user);
        when(connexionService.getConnexionsByUser(user)).thenReturn(connexions);

        assertThat(pmbSharedService.getUserConnexion().size()).isEqualTo(1);

    }

}
