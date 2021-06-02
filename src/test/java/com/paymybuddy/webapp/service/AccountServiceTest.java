package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.DTO.AccountDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.implementation.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private BankMovementService bankMovementService;

    @MockBean
    private CallBankService callBankService;

    @Autowired
    private AccountServiceImpl accountService;

    private static PMBUser user;
    private static BankAccount bankAccount;
    private static AccountDTO accountDTO;


    @BeforeEach
    public void initTest() {
        user = new PMBUser();
        user.setUserId(1L);
        user.setEmail("user@mail.com");
        user.setBalance(400.0);

        bankAccount = new BankAccount();
        bankAccount.setBankAccountId(1L);
        bankAccount.setUser(user);
        //bankAccount.setBankMovements();

        accountDTO = new AccountDTO();
        accountDTO.setBankAccountId(1L);
        accountDTO.setDebitCredit(-1);
        accountDTO.setAmount(100.0);
    }

    @Test
    public void operateMovementTest() {
        when(bankAccountService.getById(1L)).thenReturn(Optional.of(bankAccount));
        when(bankMovementService.createMovement(any(BankMovement.class))).thenReturn(new BankMovement());
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class))).thenReturn(Response.OK);
        when(pmbUserService.updateUserBalance(user, -100.0)).thenReturn(user);

        Response response = accountService.operateMovement(accountDTO);

        verify(bankAccountService, times(1)).getById(1L);
        verify(bankMovementService, times(1)).createMovement(any(BankMovement.class));
        verify(callBankService, times(1)).sendBankMovement(any(BankExchangeDTO.class));
        verify(pmbUserService, times(1)).updateUserBalance(user, -100.0);
        assertThat(response).isEqualTo(Response.OK);

    }

    @Test
    public void operateMovementDATA_ISSUETest() {
        when(bankAccountService.getById(1L)).thenReturn(Optional.empty());

        Response response = accountService.operateMovement(accountDTO);

        verify(bankAccountService, times(1)).getById(1L);
        assertThat(response).isEqualTo(Response.DATA_ISSUE);

    }

    @Test
    public void operateMovementNOT_ENOUGH_MONEYTest() {
        accountDTO.setAmount(1000.0);
        when(bankAccountService.getById(1L)).thenReturn(Optional.of(bankAccount));

        Response response = accountService.operateMovement(accountDTO);

        verify(bankAccountService, times(1)).getById(1L);
        assertThat(response).isEqualTo(Response.NOT_ENOUGH_MONEY);

    }

    @Test
    public void operateMovementSAVE_KOTest() {
        when(bankAccountService.getById(1L)).thenReturn(Optional.of(bankAccount));
        when(bankMovementService.createMovement(any(BankMovement.class))).thenReturn(new BankMovement());
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class))).thenReturn(Response.OK);
        when(pmbUserService.updateUserBalance(user, -100.0)).thenThrow(RuntimeException.class);

        Response response = accountService.operateMovement(accountDTO);

        verify(bankAccountService, times(1)).getById(1L);
        verify(bankMovementService, times(1)).createMovement(any(BankMovement.class));
        verify(callBankService, times(1)).sendBankMovement(any(BankExchangeDTO.class));
        verify(pmbUserService, times(1)).updateUserBalance(user, -100.0);
        assertThat(response).isEqualTo(Response.SAVE_KO);

    }

    @Test
    public void registerMovementBankExchangeKOTest() {
        when(bankMovementService.createMovement(any(BankMovement.class)))
                .thenReturn(new BankMovement());
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class)))
                .thenReturn(Response.BANK_EXCHANGE_ISSUE);

        Exception exception = assertThrows(RuntimeException.class, ()
                -> accountService.registerMovement(accountDTO, bankAccount));

        //THEN
        assertThat(exception.getMessage()).contains("A problem occurred during the exchange with your bank");
    }

}
