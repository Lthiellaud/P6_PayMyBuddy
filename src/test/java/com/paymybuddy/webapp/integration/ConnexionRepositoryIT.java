package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.repository.PMBUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/schema.sql")
@Sql("/sql/data.sql")
public class ConnexionRepositoryIT {

    @Autowired
    private ConnexionRepository connexionRepository;

    @Autowired
    private PMBUserRepository userRepository;

    private static List<Connexion> connexions;

    @Test
    public void findAllByUserTest() {
        Optional<PMBUser> user = userRepository.findById(1L);
        connexions = new ArrayList<>();
        user.ifPresent(pmbUser -> connexions = connexionRepository.findAllByPmbUser(pmbUser));
        System.out.println("IT Test " + connexions.size());
        assertThat(connexions.size()).isEqualTo(2);
        assertThat(connexions.get(0).getBeneficiaryUser().getUserId()).isIn(2L, 3L);

    }

    @Test
    public void findConnexionByBeneficiaryUserAndUserTest() {
        Optional<PMBUser> user = userRepository.findById(1L);
        Optional<PMBUser> beneficiaryUser = userRepository.findById(2L);
        Optional<Connexion> connexion = Optional.empty();
        if (user.isPresent() && beneficiaryUser.isPresent()) {
            connexion = connexionRepository
                    .findByBeneficiaryUserAndPmbUser(beneficiaryUser.get(), user.get());
        }

        assertThat(connexion.isPresent()).isTrue();
        assertThat(connexion.get().getConnexionId()).isEqualTo(1L);

    }

    @Test
    public void findByConnexionNameAndPmbUserTest() {
        Optional<PMBUser> user = userRepository.findById(1L);
        Optional<Connexion> connexion = Optional.empty();
        if (user.isPresent()) {
            connexion = connexionRepository.findByConnexionNameAndPmbUser("My connexion to Tom", user.get());
        }
        assertThat(connexion.isPresent()).isTrue();
        assertThat(connexion.get().getConnexionId()).isEqualTo(1L);
    }

}
