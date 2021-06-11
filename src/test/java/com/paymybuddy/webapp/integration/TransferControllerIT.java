package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.constants.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
public class TransferControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @WithMockUser
    @Test
    public void transferMoneyTest() throws Exception {
       mockMvc.perform(post("/home/transfer")
                    .param("connexionId", "1")
                    .param("description", "test")
                    .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasNoErrors())
               .andExpect(model().attribute("message", Response.OK.getMessage()));
    }

    @WithMockUser
    @Test
    public void transferMoneyWithNonExistingConnexion0Test() throws Exception {
        mockMvc.perform(post("/home/transfer")
                .param("connexionId", "10")
                .param("description", "test")
                .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.DATA_ISSUE.getMessage()));

    }

    @WithMockUser
    @Test
    public void transferMoneyWithAmount200ToBigTest() throws Exception {
        mockMvc.perform(post("/home/transfer")
                .param("connexionId", "1")
                .param("description", "test")
                .param("amount", "200"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("transfer", "amount"));


    }

}
