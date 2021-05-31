package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.PMBUserDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.implementation.PMBUserServiceImpl;
import com.paymybuddy.webapp.service.implementation.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegistrationServiceImplTest {

    @Autowired
    private RegistrationServiceImpl registrationService;

    @MockBean
    private PMBUserServiceImpl pmbUserService;

    private static PMBUserDTO userDTO;
    private static PMBUser pmbUser;

    @BeforeEach
    public void initTest() {
        userDTO = new PMBUserDTO();
        userDTO.setEmail("test@mail.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("Registration");
        userDTO.setPassword("ABCDEF01");
        userDTO.setRepeatPassword("ABCDEF01");

        pmbUser = new PMBUser();
        pmbUser.setUserId(1L);
        pmbUser.setEmail("test@mail.com");

    }

    @Test
    void registerTest() {
        when(pmbUserService.getByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(pmbUserService.saveUser(any(PMBUser.class))).thenReturn(pmbUser);

        Response response = registrationService.register(userDTO);

        assertThat(response).isEqualTo(Response.REGISTERED);

    }

    @Test
    void registerExistingUserTest() {
        when(pmbUserService.getByEmail("test@mail.com")).thenReturn(Optional.of(pmbUser));

        Response response = registrationService.register(userDTO);

        assertThat(response).isEqualTo(Response.EXISTING_USER);

    }

    @Test
    void registerWithDifferentRepeatPasswordTest() {
        userDTO.setRepeatPassword("differentPW");

        Response response = registrationService.register(userDTO);

        assertThat(response).isEqualTo(Response.MISMATCH_PASSWORD);

    }

}