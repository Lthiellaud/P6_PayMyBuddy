package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * gives access to Connexion records.
 */
@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {

    /**
     * To retrieve a list of connexions from a PMB user.
     * @param user the given PMB user
     * @return the list of connexion
     */
    List<Connexion> findAllByPmbUser(PMBUser user);

    /**
     * To retrieve a connexion from a beneficiary and a PMB user.
     * @param beneficiaryUser the beneficiary user
     * @param user the PMB user
     * @return the connexion if founded
     */
    Optional<Connexion> findByBeneficiaryUserAndPmbUser(PMBUser beneficiaryUser, PMBUser user);

    /**
     * To retrieve a connexion from a connexion name and PMB user
     * @param connexionName the connexion name
     * @param user the PMB user
     * @return the connexion if founded
     */
    Optional<Connexion> findByConnexionNameAndPmbUser(String connexionName, PMBUser user);

}
