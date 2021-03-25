package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Connexion;

import java.util.List;
import java.util.Optional;

public interface ConnexionService {

    List<Connexion> getConnexionsByUser(PMBUser user);

    Connexion createConnexion(Connexion connexion);


    Optional<Connexion> getById(Long connexionId);
}
