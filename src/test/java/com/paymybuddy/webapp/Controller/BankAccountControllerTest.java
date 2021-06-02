package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.BankAccountController;
import com.paymybuddy.webapp.model.BankAccount;
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

@WebMvcTest(controllers = BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PMBUserService pmbUserService;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getBankAccountPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/home/bankAccount"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));

    }

    @WithMockUser
    @Test
    public void getBankAccountPageTest() throws Exception {
        List<BankAccount> bankAccountList = new ArrayList<>();
        mockMvc.perform(get("/home/bankAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("bankAccounts", bankAccountList));

    }

    @WithMockUser
    @Test
    public void addBankAccountWithBlankNameTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "")
                .param("accountHolder", "USER")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "bankAccountName", "NotBlank"));

    }

    @WithMockUser
    @Test
    public void addBankAccountWithBlankOwnerTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My RIB")
                .param("accountHolder", "")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "accountHolder", "NotBlank"));

    }

    @WithMockUser
    @Test
    public void addBankAccountWithFormatIssuesTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My RIB")
                .param("accountHolder", "USER")
                .param("countryCode", "")
                .param("bankCode", "111112")
                .param("branchCode", "111k")
                .param("accountCode", "1234567890fm")
                .param("key", "ab")
                .param("bic", "ABCDEFGH1225"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "countryCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "bankCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "branchCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "accountCode", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "key", "Pattern"))
                .andExpect(model().attributeHasFieldErrorCode("bankAccountDTO", "bic", "Pattern"));

    }


}
