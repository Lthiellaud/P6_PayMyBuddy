package com.paymybuddy.webapp.controller;

import org.apache.maven.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RegsitrationController {
    
    @GetMapping("/register")
    public String registerForm(Model model) {

        return "registrationPage";
    }
    
}
