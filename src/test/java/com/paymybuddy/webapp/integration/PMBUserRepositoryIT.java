package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class PMBUserRepositoryIT {

    @Autowired
    private PMBUserRepository pmbUserRepository;

    private static PMBUser pmbUser;

    @Test
    public void findUserByEmailTest() {
        pmbUser = pmbUserRepository.findUserByEmail("lol.buddy@mail.com");

        assertThat(pmbUser.getEmail()).isEqualTo("lol.buddy@mail.com");
    }
}
