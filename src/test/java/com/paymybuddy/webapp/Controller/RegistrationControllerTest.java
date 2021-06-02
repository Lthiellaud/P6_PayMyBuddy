package com.paymybuddy.webapp.Controller;

import com.paymybuddy.webapp.controller.RegistrationController;
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

@WebMvcTest(controllers = RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void getRegistrationPageTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }


    @Test
    public void registerMissingMandatoryFieldsEmailTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("email", "")
                .param("firstName", "prénom")
                .param("lastName", "nom")
                .param("password", "ABCD1234")
                .param("repeatPassword", "ABCD123456789123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "email", "NotBlank"));

    }

    @Test
    public void registerMissingMandatoryFieldsFirstNameTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("email", "test@mail.com")
                .param("firstName", "")
                .param("lastName", "Nom")
                .param("password", "ABCD1234")
                .param("repeatPassword", "ABCD123456789123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "firstName", "NotBlank"));

    }

    @Test
    public void registerMissingMandatoryFieldsLastNameTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("email", "test@mail.com")
                .param("firstName", "prénom")
                .param("lastName", "")
                .param("password", "ABCD1234")
                .param("repeatPassword", "ABCD123456789123"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "lastName", "NotBlank"));

    }

    @Test
    public void registerPasswordTooShortTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("email", "new.buddy@mail.com")
                .param("firstName", "New")
                .param("lastName", "Buddy")
                .param("password", "ABCD")
                .param("repeatPassword", "ABCD"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "password", "Size"));
        }

    @Test
    public void registerPasswordTooLargeTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("email", "new.buddy@mail.com")
                .param("firstName", "New")
                .param("lastName", "Buddy")
                .param("password", "012345678901234567")
                .param("repeatPassword", "012345678901234567"))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationPage"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("userDTO", "password", "Size"));
    }

}
