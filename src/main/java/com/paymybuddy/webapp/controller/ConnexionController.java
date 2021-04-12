package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Connexion;
import com.paymybuddy.webapp.model.DTO.ConnexionDTO;
import com.paymybuddy.webapp.model.DTO.TransferDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.ConnexionService;
import com.paymybuddy.webapp.service.PMBSharedService;
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
        if (response == Response.OK) {
            return "redirect:connexion";
        }
        return "connexionPage";
    }
    
}
