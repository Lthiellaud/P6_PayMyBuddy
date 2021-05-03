package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.Rib;

import java.util.List;

public interface PMBSharedService {
    List<Connexion> getUserConnexion();
    List<Rib> getUserRib();

}
