package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;

import java.util.List;
import java.util.Optional;

/**
 * Service used by connexion controller and to manage Connexion data
 */
public interface ConnexionService {

    /**
     * gives the list of connexion of a user
     * @param user the given user
     * @return the list of founded connexion
     */
    List<Connexion> getConnexionsByUser(PMBUser user);

    /**
     * Creates a new connexion
     * @param connexion the connexion to be created
     * @return the created connexion
     */
    Connexion createConnexion(Connexion connexion);

    /**
     * gives a connexion from an id
     * @param connexionId the given id
     * @return the connexion if founded
     */
    Optional<Connexion> getById(Long connexionId);

    /**
     * gives a connexion from a beneficiary and a user
     * @param beneficiary the given beneficiary
     * @param user the given user
     * @return the connexion if founded
     */
    Optional<Connexion> getByBeneficiaryAndUser(PMBUser beneficiary, PMBUser user);

    /**
     * gives a connexion from a connexion name and a user
     * @param connexionName the given connexion name
     * @param user the given user
     * @return the connexion if founded
     */
    Optional<Connexion> getByConnexionNameAndUser(String connexionName, PMBUser user);

    /**
     * Verifies data and register a new connexion
     * @param connexionDTO data entered by the user
     * @return response of the operation
     */
    Response processConnexion(ConnexionDTO connexionDTO);
}
