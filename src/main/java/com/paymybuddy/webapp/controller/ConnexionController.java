package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBSharedService;
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


@Controller
public class ConnexionController {

    @Autowired
    private ConnexionService connexionService;
    
    @Autowired
    private PMBSharedService pmbSharedService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnexionController.class);

    @ModelAttribute("connexionDTO")
    public ConnexionDTO getConnexionDTOObject(){
        return new ConnexionDTO();
    }

    @GetMapping("/home/connexion")
    public String getConnexion (Model model) {
        List<Connexion> connexions = pmbSharedService.getUserConnexion();
        model.addAttribute("connexions", connexions);
        return "connexionPage";
    }
    
    @PostMapping("/home/connexion")
    public String addConnexion(@ModelAttribute ("connexionDTO") @Valid ConnexionDTO connexionDTO,
                               final BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return getConnexion(model);
        }
        Response response= connexionService.processConnexion(connexionDTO);
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
            default:
        }
        return getConnexion(model);
    }
    
}
