package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class TransferServiceIT {
    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private ConnexionService connexionService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransactionRepository transactionRepository;

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
        user.setConnexions(new HashSet<>());

        beneficiary = new PMBUser();
        beneficiary.setUserId(2L);
        beneficiary.setEmail("beneficiary@mail.com");
        beneficiary.setBalance(40.0);
        beneficiary.setConnexions(new HashSet<>());

        transferDTO = new TransferDTO();
        transferDTO.setConnexionId(1L);
        transferDTO.setDescription("The transaction");
        transferDTO.setAmount(10.0);

        connexion = new Connexion();
        connexion.setConnexionId(1L);
        connexion.setBeneficiaryUser(beneficiary);
        connexion.setPmbUser(user);
        connexion.setConnexionName("The connexion");


    }

    @Test
    public void transferWithExceptionOnUserSaveShouldNotBeRegisteredTest() {
        //Response TransferDTO transferDTO, Connexion connexion
        transactionService.deleteAllTransaction();
        when(pmbUserService.updateUserBalance(user, -10.0)).thenReturn(user);
        when(pmbUserService.updateUserBalance(beneficiary, 10.0)).thenThrow(RuntimeException.class);

        Response response = Response.SAVE_KO;
        try {
            response = transferService.registerTransfer(transferDTO, connexion);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        List<Transaction> transactions = transactionService.getAllTransactions();

        assertThat(response).isEqualTo(Response.SAVE_KO);
        assertThat(transactions.size()).isEqualTo(0);

    }


}
