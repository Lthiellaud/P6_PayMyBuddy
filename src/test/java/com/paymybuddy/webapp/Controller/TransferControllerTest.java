package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.TransferController;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.implementation.TransferServiceImpl;
import com.paymybuddy.webapp.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferServiceImpl transferService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private PMBSharedService pmbSharedService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getTransferPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/home/transfer"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @WithMockUser
    @Test
    public void transferMoneyWithoutChosenConnexionTest() throws Exception {
       mockMvc.perform(post("/home/transfer")
                    .param("connexionId", "0")
                    .param("description", "test")
                    .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("transfer", "connexionId", "Positive"));


    }

    @WithMockUser
    @Test
    public void transferMoneyWithoutDescriptionTest() throws Exception {
        mockMvc.perform(post("/home/transfer")
                .param("connexionId", "1")
                .param("description", "")
                .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("transfer", "description", "NotBlank"));


    }

    @WithMockUser
    @Test
    public void transferMoneyWithAmount0Test() throws Exception {
        mockMvc.perform(post("/home/transfer")
                .param("connexionId", "1")
                .param("description", "test")
                .param("amount", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("transferPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("transfer", "amount", "Positive"));

    }

}
