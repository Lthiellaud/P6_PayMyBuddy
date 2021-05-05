package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.ConnexionController;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBSharedService;
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

@WebMvcTest(controllers = ConnexionController.class)
public class ConnexionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private ConnexionService connexionService;

    @MockBean
    private PMBSharedService pmbSharedService;

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
                .param("connexionName", "")
                .param("connexionMail", "lol.buddy@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("connexionDTO", "connexionName", "NotBlank"));

    }

    @WithMockUser
    @Test
    public void addConnexionWithEmptyMailTest() throws Exception {
        mockMvc.perform(post("/home/connexion")
                .param("connexionName", "The Connexion")
                .param("connexionMail", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("connexionPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("connexionDTO", "connexionMail", "NotBlank"));

    }

}
