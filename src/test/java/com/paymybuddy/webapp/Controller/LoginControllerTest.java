package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.LoginController;
import com.paymybuddy.webapp.controller.RegistrationController;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.RegistrationService;
import com.paymybuddy.webapp.service.SecurityService;
import com.paymybuddy.webapp.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private PMBUserService pmbUserService;

    @Test
    public void getLoginPageTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void getSitePageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void getHomePageTest() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }


}
