package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnexionServiceImpl implements ConnexionService {

    @Autowired
    private ConnexionRepository connexionRepository;

    @Autowired
    private PMBUserService pmbUserService;

    @Override
    public List<Connexion> getConnexionsByUser(PMBUser user) {
        List<Connexion> connexions = connexionRepository.findAllByPmbUser(user);
        return connexions;
    }

    @Override
    public Connexion createConnexion(Connexion connexion) {

        return connexionRepository.save(connexion);

    }

    @Override
    public Optional<Connexion> getById(Long connexionId) {
        return connexionRepository.findById(connexionId);
    }

    @Override
    public Optional<Connexion> getByBeneficiaryAndUser(PMBUser beneficiary, PMBUser user) {
        return connexionRepository.findByBeneficiaryUserAndPmbUser(beneficiary, user);
    }

    @Override
    public Optional<Connexion> getByConnexionNameAndUser(String connexionName, PMBUser user) {
        return connexionRepository.findByConnexionNameAndPmbUser(connexionName, user);
    }

    @Override
    public Response processConnexion(ConnexionDTO connexionDTO) {
        PMBUser beneficiary = pmbUserService.getByEmail(connexionDTO.getConnexionMail());
        if (beneficiary == null) {
            return Response.MAIL_NOT_FOUND;
        }
        PMBUser currentUser = pmbUserService.getCurrentUser();
        if (getByBeneficiaryAndUser(beneficiary, currentUser).isPresent()) {
            return Response.EXISTING_CONNEXION;
        }
        if (getByConnexionNameAndUser(connexionDTO.
                getConnexionName(), currentUser).isPresent()) {
            return Response.EXISTING_CONNEXION_NAME;
        }
        Connexion newConnexion = new Connexion();
        newConnexion.setConnexionName(connexionDTO.getConnexionName());
        newConnexion.setPmbUser(currentUser);
        newConnexion.setBeneficiaryUser(beneficiary);
        createConnexion(newConnexion);

        return Response.OK;
    }



}
