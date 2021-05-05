package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.constants.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConnexionControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getConnexionPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/home/connexion"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @WithMockUser
    @Test
    public void getConnexionPageTest() throws Exception {
        List<Connexion> connexionList = new ArrayList<>();
        mockMvc.perform(get("/home/connexion"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("connexions", connexionList));

    }

    @WithMockUser
    @Test
    public void addConnexionTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "The Connexion")
                .param("connexionMail", "lol.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.SAVE_KO.getMessage()));

    }
    @WithMockUser
    @Test
    public void addConnexionWithNonExistingMailTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "The Connexion")
                .param("connexionMail", "non.existing.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("connexionDTO", "connexionMail"));

    }

}
