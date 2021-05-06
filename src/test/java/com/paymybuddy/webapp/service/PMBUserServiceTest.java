package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.implementation.PMBUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PMBUserServiceTest {

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

        user = pmbUserService.updateUserBalance(user, -10.0);

        assertThat(user.getBalance()).isEqualTo(30.0);

    }
}
