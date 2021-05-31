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
public class RegistrationControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void registrationTest() throws Exception {
       mockMvc.perform(post("/register")
                    .param("email", "new.buddy@mail.com")
                    .param("firstName", "New")
                    .param("lastName", "Buddy")
                    .param("password", "ABCD0123")
                    .param("repeatPassword", "ABCD0123"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/home"))
                .andExpect(model().hasNoErrors());

    }

}
