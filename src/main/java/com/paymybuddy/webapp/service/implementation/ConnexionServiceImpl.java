package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.controller.ConnexionController;
import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.repository.ConnexionRepository;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnexionController.class);

    @Override
    public List<Connexion> getConnexionsByUser(PMBUser user) {
        return connexionRepository.findAllByPmbUser(user);
    }

    @Override
    public Connexion createConnexion(Connexion connexion) throws Exception {
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
        Optional<PMBUser> b = pmbUserService.getByEmail(connexionDTO.getConnexionMail());
        PMBUser currentUser = pmbUserService.getCurrentUser();
        if (!b.isPresent()) {
            LOGGER.debug(Response.MAIL_NOT_FOUND + ": current user : " + currentUser.getEmail()
                    + ", beneficiary mail to be found : " + connexionDTO.getConnexionMail());
            return Response.MAIL_NOT_FOUND;
        }
        PMBUser beneficiary = b.get();
        if (getByBeneficiaryAndUser(beneficiary, currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_CONNEXION + ": current user : " + currentUser.getEmail()
                    + ", beneficiary : " + beneficiary.getEmail()
                    + ", connexionId : "
                    + getByBeneficiaryAndUser(beneficiary, currentUser).get().getConnexionId());
            return Response.EXISTING_CONNEXION;
        }
        if (getByConnexionNameAndUser(connexionDTO.
                getConnexionName(), currentUser).isPresent()) {
            LOGGER.debug(Response.EXISTING_CONNEXION_NAME + ": current user : " + currentUser.getEmail()
                    + ", connexion mail to be added : " + connexionDTO.getConnexionName());
            return Response.EXISTING_CONNEXION_NAME;
        }
        Connexion newConnexion = new Connexion();
        newConnexion.setConnexionName(connexionDTO.getConnexionName());
        newConnexion.setPmbUser(currentUser);
        newConnexion.setBeneficiaryUser(beneficiary);
        try {
            createConnexion(newConnexion) ;
        }
        catch (Exception e) {
            return Response.SAVE_KO;
        }

        return Response.OK;
    }



}
