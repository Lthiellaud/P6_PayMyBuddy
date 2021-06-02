package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * To manage the URL /home/connexion (GET/POST)
 * To add connexions (beneficiaries) to one PMB Account
 */
@Controller
public class ConnexionController {

    @Autowired
    private ConnexionService connexionService;
    
    @Autowired
    private PMBUserService pmbUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnexionController.class);

    @ModelAttribute("connexionDTO")
    public ConnexionDTO getConnexionDTOObject(){
        return new ConnexionDTO();
    }

    @GetMapping("/home/connexion")
    public String getConnexionPage(Model model) {
        PMBUser user = pmbUserService.getCurrentUser();
        LOGGER.debug("get /home/connexion - Access");

        List<Connexion> connexions = connexionService.getConnexionsByUser(user);
        LOGGER.debug("get /home/connexion - Number of Connexions : " + connexions.size());

        model.addAttribute("connexions", connexions);

        return "connexionPage";
    }
    
    @PostMapping("/home/connexion")
    public String addConnexion(@ModelAttribute ("connexionDTO") @Valid ConnexionDTO connexionDTO,
                               final BindingResult bindingResult, Model model) {
        LOGGER.debug("post /home/connexion - Connexion name : " + connexionDTO.getConnexionName()
                + ", mail to be connected : " + connexionDTO.getConnexionMail());
        if (bindingResult.hasErrors()) {
            LOGGER.debug("post /home/connexion - entry errors detected");
            return getConnexionPage(model);
        }
        Response response= connexionService.processConnexion(connexionDTO);
        LOGGER.debug("post /home/connexion - operation result : " + response);
        switch (response) {
            case OK:
                model.addAttribute("connexionDTO", new ConnexionDTO());
                model.addAttribute("message", response.getMessage());
                break;
            case EXISTING_CONNEXION_NAME:
                bindingResult.rejectValue("connexionName", "error.connexionDTO", response.getMessage());
                break;
            case EXISTING_CONNEXION:
            case MAIL_NOT_FOUND:
                bindingResult.rejectValue("connexionMail", "error.connexionDTO", response.getMessage());
                break;
            case SAVE_KO:
                model.addAttribute("message", response.getMessage());
                break;
            default:
        }
        LOGGER.debug("post /home/connexion - bindingResult : " + bindingResult.hasErrors());
        return getConnexionPage(model);
    }
    
}
