package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.DTO.PMBUserDTO;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.RegistrationService;
import com.paymybuddy.webapp.service.SecurityService;
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


@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SecurityService securityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @ModelAttribute("userDTO")
    public PMBUserDTO getUserDTOObject(){
        return new PMBUserDTO();
    }

    @GetMapping("/register")
    public String registerForm(Model model) {

        return "registrationPage";
    }

    @PostMapping("/register")
    public String userRegistration(@ModelAttribute ("userDTO") @Valid PMBUserDTO userDTO,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registrationPage";
        }

        Response response= registrationService.register(userDTO);
        LOGGER.debug("Registration try for " + userDTO.getEmail() + " - Result:" + response);
        switch (response) {
            case REGISTERED:
                securityService.autoLogin(userDTO.getEmail(), userDTO.getPassword());
                return "redirect:/home";
            case EXISTING_USER:
                bindingResult.rejectValue("email", "error.userDTO",response.getMessage());
                break;
            case MISMATCH_PASSWORD:
                bindingResult.rejectValue("repeatPassword", "error.userDTO",response.getMessage());
        }
        return "registrationPage";
    }


}
