package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConnexionServiceTest {

    @MockBean
    private ConnexionRepository connexionRepository;

    @MockBean
    private PMBUserService pmbUserService;

    @Autowired
    private ConnexionService connexionService;

    private static PMBUser currentUser;
    private static PMBUser beneficiary;
    private static ConnexionDTO connexionDTO;
    private static Connexion connexion;

    @BeforeEach
    public void initTest() {
        currentUser = new PMBUser();
        currentUser.setUserId(1L);
        currentUser.setEmail("current.user@mail.com");
        currentUser.setBalance(40.0);
        Set<Connexion> connexions = new HashSet<>();


        beneficiary = new PMBUser();
        beneficiary.setUserId(2L);
        beneficiary.setEmail("beneficiary@mail.com");
        beneficiary.setBalance(40.0);
        beneficiary.setConnexions(connexions);

        connexionDTO = new ConnexionDTO();
        connexionDTO.setConnexionName("The connexion");
        connexionDTO.setConnexionMail("beneficiary@mail.com");

        connexion = new Connexion();
        connexion.setConnexionId(1L);
        connexion.setBeneficiaryUser(beneficiary);
        connexion.setUser(currentUser);
        connexion.setConnexionName("The connexion");

    }

    @Test
    public void addProcessConnexionTest() {
        when(pmbUserService.getByEmail("beneficiary@mail.com")).thenReturn(beneficiary);
        when(connexionRepository.findByBeneficiaryUserAndUser(beneficiary, currentUser)).thenReturn(Optional.empty());
        when(connexionRepository.save(any(Connexion.class))).thenReturn(connexion);

        Response response = connexionService.processConnexion(connexionDTO);

        assertThat(response).isEqualTo(Response.OK);
    }

}
