package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {

    List<Connexion> findAllByPmbUser(PMBUser user);


    Optional<Connexion> findByBeneficiaryUserAndPmbUser(PMBUser beneficiaryUser, PMBUser user);

    Optional<Connexion> findByConnexionNameAndPmbUser(String connexionName, PMBUser user);

}
