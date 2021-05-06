package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.BankExchangeDTO;
import com.paymybuddy.webapp.model.DTO.OperationDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.implementation.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private RibService ribService;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private BankMovementService bankMovementService;

    @MockBean
    private CallBankService callBankService;

    @Autowired
    private AccountServiceImpl accountService;

    private static PMBUser user;
    private static Rib rib;
    private static OperationDTO operationDTO;


    @BeforeEach
    public void initTest() {
        user = new PMBUser();
        user.setUserId(1L);
        user.setEmail("user@mail.com");
        user.setBalance(400.0);

        rib = new Rib();
        rib.setRibId(1L);
        rib.setUser(user);
        //rib.setBankMovements();

        operationDTO = new OperationDTO();
        operationDTO.setRibId(1L);
        operationDTO.setDebitCredit(-1);
        operationDTO.setAmount(100.0);
    }

    @Test
    public void operateMovementTest() {
        when(ribService.getById(1L)).thenReturn(Optional.of(rib));
        when(bankMovementService.createMovement(any(BankMovement.class))).thenReturn(new BankMovement());
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class))).thenReturn(Response.OK);
        when(pmbUserService.updateUserBalance(user, -100.0)).thenReturn(user);

        Response response = accountService.operateMovement(operationDTO);

        verify(ribService, times(1)).getById(1L);
        verify(bankMovementService, times(1)).createMovement(any(BankMovement.class));
        verify(callBankService, times(1)).sendBankMovement(any(BankExchangeDTO.class));
        verify(pmbUserService, times(1)).updateUserBalance(user, -100.0);
        assertThat(response).isEqualTo(Response.OK);

    }

    @Test
    public void operateMovementDATA_ISSUETest() {
        when(ribService.getById(1L)).thenReturn(Optional.empty());

        Response response = accountService.operateMovement(operationDTO);

        verify(ribService, times(1)).getById(1L);
        assertThat(response).isEqualTo(Response.DATA_ISSUE);

    }

    @Test
    public void operateMovementNOT_ENOUGH_MONEYTest() {
        operationDTO.setAmount(1000.0);
        when(ribService.getById(1L)).thenReturn(Optional.of(rib));

        Response response = accountService.operateMovement(operationDTO);

        verify(ribService, times(1)).getById(1L);
        assertThat(response).isEqualTo(Response.NOT_ENOUGH_MONEY);

    }

    @Test
    public void operateMovementSAVE_KOTest() {
        when(ribService.getById(1L)).thenReturn(Optional.of(rib));
        when(bankMovementService.createMovement(any(BankMovement.class))).thenReturn(new BankMovement());
        when(callBankService.sendBankMovement(any(BankExchangeDTO.class))).thenReturn(Response.OK);
        when(pmbUserService.updateUserBalance(user, -100.0)).thenThrow(RuntimeException.class);

        Response response = accountService.operateMovement(operationDTO);

        verify(ribService, times(1)).getById(1L);
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
                -> accountService.registerMovement(operationDTO, rib));

        //THEN
        assertThat(exception.getMessage()).contains("A problem occurred during the exchange with your bank");
    }

}
