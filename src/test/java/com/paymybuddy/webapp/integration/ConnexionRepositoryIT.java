package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConnexionRepositoryIT {

    @Autowired
    private ConnexionRepository connexionRepository;

    @Autowired
    private PMBUserRepository userRepository;

    private static List<Connexion> connexions;

    @Test
    public void testExistingFindAllByUser() {
        Optional<PMBUser> user = userRepository.findById(1L);
        connexions = new ArrayList<>();
        user.ifPresent(pmbUser -> connexions = connexionRepository.findAllByUser(pmbUser));
        System.out.println("IT Test " + connexions.size());
        assertThat(connexions.size()).isEqualTo(2);

    }
}
