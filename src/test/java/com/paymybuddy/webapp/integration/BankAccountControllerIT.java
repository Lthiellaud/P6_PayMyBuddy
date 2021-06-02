package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.constants.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @WithUserDetails("tom.buddy@mail.com")
    @Test
    public void addBankAccountTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My RIB")
                .param("accountHolder", "Tom Buddy")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.OK.getMessage()));

    }

    @WithMockUser
    @Test
    public void addBankAccountSaveKOTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My RIB")
                .param("accountHolder", "USER")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.SAVE_KO.getMessage()));

    }

    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void addBankAccountWithExistingIBANTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My new RIB")
                .param("accountHolder", "USER")
                .param("countryCode", "fr76")
                .param("bankCode", "22222")
                .param("branchCode", "88888")
                .param("AccountCode", "9999977777x")
                .param("key", "00")
                .param("bic", "XXXXXXXX"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.EXISTING_IBAN.getMessage()));

    }

    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void addBankAccountWithExistingNameTest() throws Exception {
        mockMvc.perform(post("/home/bankAccount")
                .param("bankAccountName", "My Rib")
                .param("accountHolder", "LOL BUDDY")
                .param("countryCode", "fr76")
                .param("bankCode", "11111")
                .param("branchCode", "11111")
                .param("AccountCode", "1234567890f")
                .param("key", "55")
                .param("bic", "ABCDEFGH"))
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("bankAccountDTO", "bankAccountName"));

    }

}
