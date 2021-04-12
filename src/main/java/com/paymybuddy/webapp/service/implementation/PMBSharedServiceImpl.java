package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBSharedService;
import com.paymybuddy.webapp.service.PMBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PMBSharedServiceImpl implements PMBSharedService {

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private ConnexionService connexionService;

    @Override
    public List<Connexion> getUserConnexion() {
        PMBUser user = pmbUserService.getCurrentUser();
        List<Connexion> connexions = connexionService.getConnexionsByUser(user);

        return connexions;
    }

}
