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
public class ConnexionControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @WithUserDetails("tom.buddy@mail.com")
    @Test
    public void addConnexionTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "Tom Connexion to Lol")
                .param("connexionMail", "lol.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.OK.getMessage()));

    }

    @WithMockUser
    @Test
    public void addConnexionSaveKOTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "The Connexion")
                .param("connexionMail", "lol.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", Response.SAVE_KO.getMessage()));

    }

    @WithUserDetails("lol.buddy@mail.com")
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

    @WithUserDetails("lol.buddy@mail.com")
    @Test
    public void addConnexionWithExistingNameTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "My connexion to Tom")
                .param("connexionMail", "madeleine.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("connexionDTO", "connexionName"));

    }

}
