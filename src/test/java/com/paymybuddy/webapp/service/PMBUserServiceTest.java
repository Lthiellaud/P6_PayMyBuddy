package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import com.paymybuddy.webapp.service.implementation.PMBUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PMBUserServiceTest {

    @MockBean
    private PMBUserRepository pmbUserRepository;

    @Autowired
    private PMBUserServiceImpl pmbUserService;

    private static PMBUser user;

    @BeforeEach
    public void initTest() {
        user = new PMBUser();
        user.setUserId(1L);
        user.setEmail("user@mail.com");
        user.setBalance(40.0);
    }

    @Test
    public void updateUserBalanceTest() {
        when(pmbUserRepository.save(user)).thenReturn(user);
        user = pmbUserService.updateUserBalance(user, -10.0);

        assertThat(user.getBalance()).isEqualTo(30.0);

    }
}
