package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.*;
import com.paymybuddy.webapp.model.DTO.AccountDTO;
import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class AccountServiceIT {
    @Autowired
    private PMBUserService pmbUserService;

    @MockBean
    private CallBankService callBankService;

    @Autowired
    private BankMovementService bankMovementService;

    @Autowired
    private AccountService accountService;

    private static AccountDTO accountDTO;
    private static BankAccount bankAccount;

    @BeforeEach
    public void initTest() {

        accountDTO = new AccountDTO();
        accountDTO.setAmount(10.0);
        accountDTO.setBankAccountId(1L);
        accountDTO.setDebitCredit(-1);

        bankAccount = new BankAccount();
        bankAccount.setBankAccountId(1L);


    }

    /**
     * To test rollback
     */
    @Test
    public void accountMovementWithExceptionShouldNotBeRegisteredTest() {
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class))).thenReturn(Response.BANK_EXCHANGE_ISSUE);

        Response response = Response.SAVE_KO;
        try {
            response = accountService.registerMovement(accountDTO, bankAccount);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        List<BankMovement> bankMovements = bankMovementService.getMovements(Arrays.asList(bankAccount));

        assertThat(response).isEqualTo(Response.SAVE_KO);
        assertThat(bankMovements.size()).isEqualTo(0);

    }


}
