package com.paymybuddy.webapp.controller;


import javax.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController

{
    @RolesAllowed("USER")
    @RequestMapping("/*")
    public String getUser()
    {
        return "Welcome User";
    }

}
