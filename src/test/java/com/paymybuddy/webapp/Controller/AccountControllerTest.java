package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.AccountController;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankMovement;
import com.paymybuddy.webapp.service.AccountService;
import com.paymybuddy.webapp.service.BankMovementService;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.BankAccountService;
import com.paymybuddy.webapp.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private BankMovementService bankMovementService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getAccountPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/home/account"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));

    }

    @WithMockUser
    @Test
    public void getAccountPageTest() throws Exception {
        List<BankAccount> bankAccountList = new ArrayList<>();
        List<BankMovement> movementList = new ArrayList<>();
        mockMvc.perform(get("/home/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("bankAccounts", bankAccountList))
                .andExpect(model().attribute("bankMovements", movementList));

    }

    @WithMockUser
    @Test
    public void manageAccountWithoutChosenBankAccountTest() throws Exception {
       mockMvc.perform(post("/home/account")
                    .param("bankAccountId", "0")
                    .param("debitCredit", "1")
                    .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("accountDTO", "bankAccountId", "Positive"));


    }

    @WithMockUser
    @Test
    public void manageAccountOperationNonSelectedTest() throws Exception {
        mockMvc.perform(post("/home/account")
                .param("bankAccountId", "1")
                .param("debitCredit", "-2")
                .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("accountDTO", "debitCredit", "Min"));


    }

    @WithMockUser
    @Test
    public void manageAccountWithAmount0Test() throws Exception {
        mockMvc.perform(post("/home/account")
                .param("bankAccountId", "1")
                .param("debitCredit", "test")
                .param("amount", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("accountDTO", "amount", "Positive"));

    }

}
