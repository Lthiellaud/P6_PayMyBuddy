package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.service.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnexionServiceImpl implements ConnexionService {

    @Autowired
    private ConnexionRepository connexionRepository;

    @Override
    public List<Connexion> getConnexionsByUser(PMBUser user) {
        List<Connexion> connexions = connexionRepository.findAllByUser(user);
        return connexions;
    }

    @Override
    public Connexion createConnexion(Connexion connexion) {
        return null;
    }

    @Override
    public Optional<Connexion> getById(Long connexionId) {
        return connexionRepository.findById(connexionId);
    }

}
