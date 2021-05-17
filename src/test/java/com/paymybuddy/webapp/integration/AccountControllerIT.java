package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.constants.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void manageAccountTest() throws Exception {
       mockMvc.perform(post("/home/account")
                    .param("ribId", "1")
                    .param("debitCredit", "1")
                    .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasNoErrors())
               .andExpect(model().attribute("message", Response.OK.getMessage()));


    }

    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void manageAccountWithNonExistingRibTest() throws Exception {
        mockMvc.perform(post("/home/account")
                .param("ribId", "10")
                .param("debitCredit", "-1")
                .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.DATA_ISSUE.getMessage()));

    }

    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void manageAccountWithAmountMinus1000Test() throws Exception {
        mockMvc.perform(post("/home/account")
                .param("ribId", "1")
                .param("debitCredit", "-1")
                .param("amount", "1000"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("operationDTO", "amount"));


    }

}
