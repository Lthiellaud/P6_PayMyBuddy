package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.constants.Response;

import java.util.List;
import java.util.Optional;

public interface ConnexionService {

    List<Connexion> getConnexionsByUser(PMBUser user);

    Connexion createConnexion(Connexion connexion);

    Optional<Connexion> getById(Long connexionId);

    Optional<Connexion> getByBeneficiaryAndUser(PMBUser beneficiary, PMBUser user);

    Optional<Connexion> getByConnexionNameAndUser(String connexionName, PMBUser user);

    Response processConnexion(ConnexionDTO connexionDTO);
}
