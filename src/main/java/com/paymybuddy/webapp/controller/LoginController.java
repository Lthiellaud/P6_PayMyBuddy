package com.paymybuddy.webapp.controller;


import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * To access Pay My Buddy
 * URL : /, /home, /login, /403
 */
@Controller
public class LoginController {

    @Autowired
    private PMBUserService pmbUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/home")
    public String getUser(Model model)
    {
        PMBUser user = pmbUserService.getCurrentUser();
        LOGGER.debug("get /home - Access");
        model.addAttribute("welcome", pmbUserService.getWelcomeMessage(user));
        model.addAttribute("balance", pmbUserService.getBalanceMessage(user));
        return "homePage";
    }

   @GetMapping("/login")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/")
    public String enter() {
        return "sitePage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        String message = "Authentication problem";
        LOGGER.debug("Access denied");
        if (principal != null) {
            User loginUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = UserUtil.toString(loginUser);

            model.addAttribute("userInfo", userInfo);

            message = "Hello " + principal.getName()
                    + "<br> You do not have permission to access this page!";
            LOGGER.debug("Access denied for " + userInfo);
        }
        model.addAttribute("message", message);
        return "403Page";
    }
}
